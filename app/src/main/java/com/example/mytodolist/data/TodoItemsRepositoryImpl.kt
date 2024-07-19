package com.example.mytodolist.data.domain

import android.content.SharedPreferences
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.example.mytodolist.data.database.AppDatabase
import com.example.mytodolist.data.database.TodoItemDao
import com.example.mytodolist.data.network.TodoApi
import com.example.mytodolist.data.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val todoItemDao: TodoItemDao,
    private val sharedPreferences: SharedPreferences
) : ITodoItemsRepository {

    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())

    private var lastKnownRevision: Int
        get() = sharedPreferences.getInt("lastKnownRevision", 0)
        set(value) {
            sharedPreferences.edit().putInt("lastKnownRevision", value).apply()
        }

    private val api = RetrofitClient.instance

    private lateinit var snackbarHostState: SnackbarHostState

    init {
        fetchAllTodoItems()
        setupPeriodicRefresh()
    }

    override fun getAllTodoItems(): Flow<List<TodoItem>> = _todoList

    override fun fetchAllTodoItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getList().execute()
                if (response.isSuccessful) {
                    response.body()?.let { todoResponse ->
                        val serverRevision = todoResponse.revision
                        if (serverRevision != lastKnownRevision) {
                            sendPatchRequest(serverRevision)
                        } else {
                            val todoItems = todoResponse.list
                            _todoList.update { todoItems }
                            saveTodoItemsToDatabase(todoItems)
                        }
                        lastKnownRevision = serverRevision
                    }
                } else {
                    handleFetchError(response)
                }
            } catch (e: IOException) {
                fetchFromDatabase()
                showRetrySnackbar("Network error") { fetchAllTodoItems() }
            } catch (e: HttpException) {
                fetchFromDatabase()
                showRetrySnackbar("Server error") { fetchAllTodoItems() }
            }
        }
    }

    private suspend fun sendPatchRequest(serverRevision: Int) {
        val localItems = todoItemDao.getAll().first()
        val updateRequest = TodoApi.UpdateListRequest(localItems)

        try {
            val response = api.updateList(updateRequest, serverRevision).execute()
            if (response.isSuccessful) {
                response.body()?.let { todoResponse ->
                    val updatedItems = todoResponse.list
                    _todoList.update { updatedItems }
                    saveTodoItemsToDatabase(updatedItems)
                    lastKnownRevision = todoResponse.revision
                }
            } else {
                showRetrySnackbar("Error updating server list") { sendPatchRequest(serverRevision) }
            }
        } catch (e: IOException) {
            showRetrySnackbar("Network error") { sendPatchRequest(serverRevision) }
        } catch (e: HttpException) {
            showRetrySnackbar("Server error") { sendPatchRequest(serverRevision) }
        }
    }

    private suspend fun fetchFromDatabase() {
        todoItemDao.getAll().collect { todoItems ->
            _todoList.update { todoItems }
        }
    }

    override fun addTodoItem(todoItem: TodoItem) {
        val addItemRequest = TodoApi.AddItemRequest(todoItem)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.addItem(addItemRequest, lastKnownRevision).execute()
                if (response.isSuccessful) {
                    response.body()?.let { item ->
                        _todoList.update { currentList -> currentList + item }
                        saveTodoItemToDatabase(todoItem)
                    }
                    lastKnownRevision++
                } else {
                    saveTodoItemToDatabase(todoItem)
                    showRetrySnackbar("Error adding item") { addTodoItem(todoItem) }
                }
            } catch (e: IOException) {
                saveTodoItemToDatabase(todoItem)
                showRetrySnackbar("Network error") { addTodoItem(todoItem) }
            } catch (e: HttpException) {
                saveTodoItemToDatabase(todoItem)
                showRetrySnackbar("Server error") { addTodoItem(todoItem) }
            }
        }
    }

    override fun updateItem(item: TodoItem) {
        val updateRequest = TodoApi.UpdateListRequest(
            list = _todoList.value.map {
                if (it.id == item.id) it.copy(
                    text = item.text.trim(),
                    importance = item.importance,
                    deadline = item.deadline,
                    done = item.done,
                    color = item.color,
                    changed_at = System.currentTimeMillis(),
                    created_at = item.created_at,
                    last_updated_by = item.last_updated_by
                ) else it
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.updateList(updateRequest, lastKnownRevision).execute()
                if (response.isSuccessful) {
                    _todoList.update { currentList ->
                        currentList.map { if (it.id == item.id) item else it }
                    }
                    updateTodoItemInDatabase(item)
                    lastKnownRevision++
                } else {
                    updateTodoItemInDatabase(item)
                    showRetrySnackbar("Server error") { updateItem(item) }
                }
            } catch (e: IOException) {
                updateTodoItemInDatabase(item)
                showRetrySnackbar("Network error") { updateItem(item) }
            } catch (e: HttpException) {
                updateTodoItemInDatabase(item)
                showRetrySnackbar("Server error") { updateItem(item) }
            }
        }
    }

    override fun deleteItem(todoItemId: String) {
        _todoList.update { list -> list.filterNot { it.id == todoItemId } }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.deleteItem(todoItemId, lastKnownRevision).execute()
                if (!response.isSuccessful) {
                    deleteTodoItemFromDatabase(todoItemId)
                    showRetrySnackbar("Server error") { deleteItem(todoItemId) }
                } else {
                    deleteTodoItemFromDatabase(todoItemId)
                    lastKnownRevision++
                }
            } catch (e: IOException) {
                deleteTodoItemFromDatabase(todoItemId)
                showRetrySnackbar("Network error") { deleteItem(todoItemId) }
            } catch (e: HttpException) {
                deleteTodoItemFromDatabase(todoItemId)
                showRetrySnackbar("Server error") { deleteItem(todoItemId) }
            }
        }
    }

    private fun handleFetchError(response: Response<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            showRetrySnackbar("Error fetching data") { fetchAllTodoItems() }
        }
    }

    private fun setupPeriodicRefresh() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(3000L)
                fetchAllTodoItems()
            }
        }
    }

    override suspend fun getItem(itemId: String): TodoItem? {
        return withContext(Dispatchers.IO) {
            _todoList.value.find { it.id == itemId }
        }
    }

    override suspend fun createItem(item: TodoItem) {
        val newItem = item.copy(
            id = UUID.randomUUID().toString(),
            text = item.text.trim(),
            created_at = System.currentTimeMillis(),
            done = false,
            changed_at = System.currentTimeMillis(),
            last_updated_by = item.last_updated_by
        )
        addTodoItem(newItem)
    }

    override fun setSnackbarHostState(state: SnackbarHostState) {
        snackbarHostState = state
    }

    private fun showRetrySnackbar(message: String, action: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Retry",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                action()
            }
        }
    }

    private suspend fun saveTodoItemsToDatabase(items: List<TodoItem>) {
        withContext(Dispatchers.IO) {
            todoItemDao.insertAll(items)
            lastKnownRevision++
        }
    }

    private suspend fun saveTodoItemToDatabase(item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoItemDao.insert(item)
            lastKnownRevision++
        }
    }

    private suspend fun updateTodoItemInDatabase(item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoItemDao.update(item)
            lastKnownRevision++
        }
    }

    private suspend fun deleteTodoItemFromDatabase(itemId: String) {
        withContext(Dispatchers.IO) {
            todoItemDao.delete(itemId)
            lastKnownRevision++
        }
    }
}