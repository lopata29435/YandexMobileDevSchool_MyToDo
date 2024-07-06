package com.example.mytodolist.Classes

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.res.stringResource
import com.example.mytodolist.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class TodoItemsRepository {
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: Flow<List<TodoItem>> = _todoList

    private val _errorFlow = MutableSharedFlow<Pair<String, suspend () -> Unit>>()
    val errorFlow: SharedFlow<Pair<String, suspend () -> Unit>> = _errorFlow

    private var lastKnownRevision: Int = 0

    private val api = RetrofitClient.instance

    private var retryJob: Job? = null
    private val retryDelay = 3000L // 3 seconds

    init {
        fetchAllTodoItems()
        setupPeriodicRefresh()
    }

    fun getAllTodoItems(): Flow<List<TodoItem>> = _todoList

    private fun fetchAllTodoItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getList().execute()
                if (response.isSuccessful) {
                    response.body()?.let { todoResponse ->
                        val todoItems = todoResponse.list
                        _todoList.update { todoItems } // Обновление _todoList
                        lastKnownRevision = todoResponse.revision
                    }
                } else {
                    handleFetchError(response)
                }
            } catch (e: IOException) {
                showRetrySnackbar("Network error") { fetchAllTodoItems() }
            } catch (e: HttpException) {
                showRetrySnackbar("Server error") { fetchAllTodoItems() }
            }
        }
    }

    fun addTodoItem(todoItem: TodoItem) {
        val addItemRequest = TodoApi.AddItemRequest(todoItem)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.addItem(addItemRequest, lastKnownRevision).execute()
                if (response.isSuccessful) {
                    response.body()?.let { item ->
                        _todoList.update { currentList -> currentList + item }
                    }
                    lastKnownRevision++
                } else {
                    showRetrySnackbar("Error adding item") { addTodoItem(todoItem) }
                }
            } catch (e: IOException) {
                showRetrySnackbar("Network error") { addTodoItem(todoItem) }
            } catch (e: HttpException) {
                showRetrySnackbar("Server error") { addTodoItem(todoItem) }
            }
        }
    }

    fun updateItem(item: TodoItem) {
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
                    lastKnownRevision++
                } else {
                    showRetrySnackbar("Server error") { updateItem(item) }
                }
            } catch (e: IOException) {
                showRetrySnackbar("Network error") { updateItem(item) }
            } catch (e: HttpException) {
                showRetrySnackbar("Server error") { updateItem(item) }
            }
        }
    }

    fun deleteItem(todoItemId: String) {
        _todoList.update { list -> list.filterNot { it.id == todoItemId } }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.deleteItem(todoItemId).execute()
                if (!response.isSuccessful) {
                    showRetrySnackbar("Server error") { deleteItem(todoItemId) }
                } else {
                    lastKnownRevision++
                }
            } catch (e: IOException) {
                showRetrySnackbar("Network error") { deleteItem(todoItemId) }
            } catch (e: HttpException) {
                showRetrySnackbar("Server error") { deleteItem(todoItemId) }
            }
        }
    }

    private suspend fun showRetrySnackbar(message: String, retryAction: suspend () -> Unit) {
        _errorFlow.emit(Pair(message, retryAction))
    }

    private fun handleFetchError(response: Response<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            showRetrySnackbar("Error fetching data") { fetchAllTodoItems() }
        }
    }

    private fun showRetrySnackbar(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            showRetrySnackbar(message) { fetchAllTodoItems() }
        }
    }

    private fun retryFetchAllTodoItems() {
        retryJob?.cancel()
        retryJob = CoroutineScope(Dispatchers.IO).launch {
            delay(retryDelay)
            fetchAllTodoItems()
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

    fun getItem(itemId: String): TodoItem? {
        return _todoList.value.find { it.id == itemId }
    }

    fun createItem(item: TodoItem) {
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

    fun size(): Int = _todoList.value.size

}