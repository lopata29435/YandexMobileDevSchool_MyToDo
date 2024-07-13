package com.example.mytodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mytodolist.presentation.navigation.MainScreen
import com.example.mytodolist.presentation.theme.MyToDoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val todoItemsRepository = (application as App).appComponent.getTodoItemsRepository()

        setContent {
            MyToDoListTheme {
                MainScreen(todoItemsRepository)
            }
        }
    }
}


