package com.example.mytodolist

import AddTaskScreen
import TodoItemElement
import TodoListElement
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.mytodolist.Classes.Priority
import com.example.mytodolist.Classes.TodoItem
import com.example.mytodolist.Classes.TodoItemsRepository
import com.example.mytodolist.ui.navigation.MainScreen
import com.example.mytodolist.ui.theme.MyToDoListTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MyToDoListTheme{
                MainScreen((application as App).data)
            }
        }
    }
}


