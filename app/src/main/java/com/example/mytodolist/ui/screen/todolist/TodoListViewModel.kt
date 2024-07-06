package com.example.mytodolist.ui.screen.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.Classes.TodoItem
import com.example.mytodolist.Classes.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TodoListViewModel(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {

    private val _todoItemsState = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItemsState: StateFlow<List<TodoItem>> = _todoItemsState.asStateFlow()

    private val _showCompleted = MutableStateFlow(true)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()

    private val _completedTasksCount = MutableStateFlow(0)
    val completedTasksCount: StateFlow<Int> = _completedTasksCount.asStateFlow()

    val filteredTodoItems: StateFlow<List<TodoItem>> = combine(
        _todoItemsState, _showCompleted
    ) { items, showCompleted ->
        if (showCompleted) items else items.filter { !it.done }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadTodoItems()
    }

    private fun loadTodoItems() {
        viewModelScope.launch {
            todoItemsRepository.getAllTodoItems().collect { items ->
                _todoItemsState.value = items
                _completedTasksCount.value = items.count { it.done }
            }
        }
    }

    fun toggleShowCompleted() {
        _showCompleted.value = !_showCompleted.value
    }

    fun updateTodoItemCompletion(id: String, done: Boolean) {
        viewModelScope.launch {
            val updatedItems = _todoItemsState.value.map { item ->
                if (item.id == id) {
                    item.copy(done = done)
                } else {
                    item
                }
            }
            _todoItemsState.value = updatedItems
            _completedTasksCount.value = updatedItems.count { it.done }
        }
    }
}
class TodoListViewModelFactory(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoListViewModel(todoItemsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}