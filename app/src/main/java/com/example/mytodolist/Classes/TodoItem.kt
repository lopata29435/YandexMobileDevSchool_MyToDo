package com.example.mytodolist.Classes

enum class Priority {
    LOW,
    DEFAULT,
    HIGH
}

data class TodoItem(
    val id: String,
    val text: String,
    val priority: Priority,
    val deadline: Long?,
    val isCompleted: Boolean,
    val createTime: Long,
    val changeTime: Long
)