package com.example.mytodolist

import android.app.Application
import com.example.mytodolist.data.TodoItemsRepository

class App : Application() {
    val data : TodoItemsRepository by lazy { TodoItemsRepository() }
}