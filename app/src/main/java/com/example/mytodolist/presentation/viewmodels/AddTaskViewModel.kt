package com.example.mytodolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.data.domain.Importance
import com.example.mytodolist.data.domain.TodoItem
import com.example.mytodolist.data.domain.ITodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


class AddTaskViewModel @Inject constructor(
    private val repository: ITodoItemsRepository,
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
        taskId?.let { id ->
            viewModelScope.launch {
                val task = repository.getItem(id)
                if (task != null) {
                    _taskDescription.value = task.text
                    _importance.value = Importance.valueOf(task.importance)
                    _deadline.value = task.deadline
                    _deleteEnabled.value = true
                    _done.value = task.done
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

    fun saveTask() {
        viewModelScope.launch {
            val newTask = TodoItem(
                id = if (taskId == "0") UUID.randomUUID().toString() else taskId!!,
                text = _taskDescription.value.trim(),
                importance = _importance.value.toString(),
                deadline = _deadline.value,
                done = _done.value,
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
        taskId?.let { id ->
            viewModelScope.launch {
                repository.deleteItem(id)
            }
        }
    }
}

class AddTaskViewModelFactory @Inject constructor(
    private val repository: ITodoItemsRepository,
    private val taskId: String?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTaskViewModel(repository, taskId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}