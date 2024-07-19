package com.example.mytodolist.data.domain

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.Flow

interface ITodoItemsRepository {
    fun getAllTodoItems(): Flow<List<TodoItem>>
    fun fetchAllTodoItems()
    fun addTodoItem(todoItem: TodoItem)
    fun updateItem(item: TodoItem)
    fun deleteItem(todoItemId: String)
    suspend fun getItem(itemId: String): TodoItem?
    suspend fun createItem(item: TodoItem)
    fun setSnackbarHostState(state: SnackbarHostState)
}