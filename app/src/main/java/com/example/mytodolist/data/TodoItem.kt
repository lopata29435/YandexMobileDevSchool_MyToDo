package com.example.mytodolist.data

enum class Importance {
    low,
    basic,
    important
}

data class TodoItem(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    val created_at: Long,
    val changed_at: Long,
    val last_updated_by: String
)

data class TodoResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)
