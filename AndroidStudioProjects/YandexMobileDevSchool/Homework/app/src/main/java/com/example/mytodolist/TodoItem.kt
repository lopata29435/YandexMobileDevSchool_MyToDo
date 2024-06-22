package com.example.mytodolist

enum class Priority {
    LOW,
    NORMAL,
    HIGH
}

data class TodoItem (
    val id : Int,
    var text : String,
    var priority : Priority,
    var deadline : Long,
    var isCompleted : Boolean,
    val createTime : Long,
    var changeTime : Long
    )