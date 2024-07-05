package com.example.mytodolist

import android.app.Application
import com.example.mytodolist.Classes.TodoItemsRepository

class App : Application() {
    val data : TodoItemsRepository by lazy { TodoItemsRepository() }
    override fun onCreate() {
        super.onCreate()
    }
}