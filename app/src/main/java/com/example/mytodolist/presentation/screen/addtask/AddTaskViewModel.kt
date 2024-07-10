package com.example.mytodolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.data.Importance
import com.example.mytodolist.data.TodoItem
import com.example.mytodolist.data.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class AddTaskViewModel(
    private val repository: TodoItemsRepository,
    private val taskId: String? = null,
) : ViewModel() {
    private val _importance = MutableStateFlow(Importance.basic)
    val importance: StateFlow<Importance> = _importance

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline: StateFlow<Long?> = _deadline

    private val _deleteEnabled = MutableStateFlow(false)
    val deleteEnabled: StateFlow<Boolean> = _deleteEnabled

    private val _taskDescription = MutableStateFlow("")
    val taskDescription: StateFlow<String> = _taskDescription

    private val _done = MutableStateFlow(false)
    val done: StateFlow<Boolean> = _done

    init {
        if (taskId != null) {
            viewModelScope.launch {
                val task = repository.getItem(taskId)
                if (task != null) {
                    _taskDescription.value = task.text
                    _importance.value = Importance.valueOf(task.importance)
                    _deadline.value = task.deadline
                    _deleteEnabled.value = true
                }
            }
        }
    }

    fun onDescriptionChange(description: String) {
        _taskDescription.value = description
    }

    fun onPriorityChange(priority: Importance) {
        _importance.value = priority
    }

    fun onDeadlineChange(deadline: Long?) {
        _deadline.value = deadline
    }
    fun onDoneChange(done: Boolean) {
        _done.value = done
    }

    fun saveTask() {
        viewModelScope.launch {
            val newTask = TodoItem(
                id = taskId ?: UUID.randomUUID().toString(),
                text = _taskDescription.value.trim(),
                importance = _importance.value.toString(),
                deadline = _deadline.value,
                done = _done.value, // используем текущее значение done из StateFlow
                created_at = System.currentTimeMillis(),
                changed_at = System.currentTimeMillis(),
                last_updated_by = "user",
                color = null
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
    private val taskId: String?,
    private val revision: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            AddTaskViewModel(todoItemsRepository, taskId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}