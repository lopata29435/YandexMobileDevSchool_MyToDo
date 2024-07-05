package com.example.mytodolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.Classes.Priority
import com.example.mytodolist.Classes.TodoItem
import com.example.mytodolist.Classes.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddTaskViewModel(
    private val repository: TodoItemsRepository,
    private val taskId: String? = null
) : ViewModel() {
    private val _priority = MutableStateFlow(Priority.DEFAULT)
    val priority: StateFlow<Priority> = _priority

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline: StateFlow<Long?> = _deadline

    private val _deleteEnabled = MutableStateFlow(false)
    val deleteEnabled: StateFlow<Boolean> = _deleteEnabled

    private val _taskDescription = MutableStateFlow("")
    val taskDescription: StateFlow<String> = _taskDescription

    init {
        if (taskId != null) {
            viewModelScope.launch {
                val task = repository.getItem(taskId)
                if (task != null) {
                    _taskDescription.value = task.text
                    _priority.value = task.priority
                    _deadline.value = task.deadline
                    _deleteEnabled.value = true
                }
            }
        }
    }

    fun onDescriptionChange(description: String) {
        _taskDescription.value = description
    }

    fun onPriorityChange(priority: Priority) {
        _priority.value = priority
    }

    fun onDeadlineChange(deadline: Long?) {
        _deadline.value = deadline
    }

    fun saveTask() {
        viewModelScope.launch {
            val newTask = TodoItem(
                id = taskId ?: UUID.randomUUID().toString(),
                text = _taskDescription.value.trim(),
                priority = _priority.value,
                deadline = _deadline.value ?: 0L,
                isCompleted = false,
                createTime = System.currentTimeMillis(),
                changeTime = System.currentTimeMillis()
            )

            if (taskId != "0") {
                repository.updateItem(newTask)
            } else {
                repository.createItem(newTask)
            }
        }
    }

    fun deleteTask() {
        if (taskId != null) {
            viewModelScope.launch {
                repository.deleteItem(taskId)
            }
        }
    }
}

class AddTaskViewModelFactory(
    private val todoItemsRepository: TodoItemsRepository,
    private val taskId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            AddTaskViewModel(todoItemsRepository, taskId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}