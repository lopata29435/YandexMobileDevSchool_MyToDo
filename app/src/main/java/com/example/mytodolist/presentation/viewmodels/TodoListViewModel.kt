package com.example.mytodolist.presentation.viewmodels

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.data.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.example.mytodolist.data.domain.ITodoItemsRepository
import com.example.mytodolist.data.domain.Theme
import com.example.mytodolist.data.domain.ThemePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    val todoItemsRepository: ITodoItemsRepository,
    val themePreferences: ThemePreferences
) : ViewModel() {

    private val _todoItemsState = MutableStateFlow<List<TodoItem>>(emptyList())

    private val _showCompleted = MutableStateFlow(true)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()

    private val _completedTasksCount = MutableStateFlow(0)
    val completedTasksCount: StateFlow<Int> = _completedTasksCount.asStateFlow()

    private val _themeMode = MutableStateFlow(themePreferences.getThemeMode())
    val themeMode: StateFlow<Theme> = _themeMode

    val filteredTodoItems: StateFlow<List<TodoItem>> = combine(
        _todoItemsState, _showCompleted
    ) { items, showCompleted ->
        if (showCompleted) items else items.filter { !it.done }
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadTodoItems()
    }

    fun updateTheme(newTheme: Theme) {
        viewModelScope.launch {
            _themeMode.emit(newTheme)
            themePreferences.setThemeMode(newTheme)
        }
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
            val itemToUpdate = _todoItemsState.value.find { it.id == id }
            if (itemToUpdate != null) {
                val updatedItem = itemToUpdate.copy(done = done)
                todoItemsRepository.updateItem(updatedItem)
            }
        }
    }
}

class TodoListViewModelFactory @Inject constructor(
    private val repository: ITodoItemsRepository,
    private val themePreferences: ThemePreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoListViewModel(repository, themePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}