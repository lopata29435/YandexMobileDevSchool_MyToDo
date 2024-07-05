package com.example.mytodolist.Classes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class TodoItemsRepository {
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: Flow<List<TodoItem>> = _todoList

    fun getAllTodoItems(): Flow<List<TodoItem>> = _todoList

    fun addTodoItem(todoItem: TodoItem) {
        _todoList.update { list ->
            list + todoItem
        }
    }

    fun updateItem(item: TodoItem) {
        _todoList.update { list ->
            list.map {
                if (it.id == item.id) it.copy(
                    text = item.text.trim(),
                    priority = item.priority,
                    deadline = item.deadline,
                    isCompleted = item.isCompleted,
                    changeTime = System.currentTimeMillis(),
                    createTime = item.createTime
                ) else it
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
            createTime = System.currentTimeMillis(),
            isCompleted = false,
            changeTime = System.currentTimeMillis(),
        )
        addTodoItem(newItem)
    }

    fun deleteItem(todoItemId: String) {
        _todoList.update { list -> list.filterNot { it.id == todoItemId } }
    }

    fun size(): Int = _todoList.value.size

    init {
        repeat(20) {
            val todoItem = TodoItem(
                id = UUID.randomUUID().toString(),
                text = "Task ${it + 1}",
                priority = Priority.entries.toTypedArray().random(),
                deadline = System.currentTimeMillis() + (1..7).random() * 24 * 60 * 60 * 1000,
                isCompleted = false,
                createTime = System.currentTimeMillis(),
                changeTime = System.currentTimeMillis()
            )
            addTodoItem(todoItem)
        }
    }
}