package com.example.mytodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodolist.data.domain.ITodoItemsRepository
import com.example.mytodolist.data.domain.Theme
import com.example.mytodolist.data.domain.ThemePreferences
import com.example.mytodolist.presentation.navigation.MainScreen
import com.example.mytodolist.presentation.theme.MyToDoListTheme
import com.example.mytodolist.presentation.viewmodels.TodoListViewModel
import com.example.mytodolist.presentation.viewmodels.TodoListViewModelFactory
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    private lateinit var themePreferences: ThemePreferences
    private lateinit var todoItemsRepository: ITodoItemsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        themePreferences = ThemePreferences(this)
        todoItemsRepository = (application as App).appComponent.getTodoItemsRepository()

        setContent {
            val todoListViewModel: TodoListViewModel = viewModel(
                factory = TodoListViewModelFactory(todoItemsRepository, themePreferences)
            )

            ThemedContent(todoListViewModel)
        }
    }
}

@Composable
fun ThemedContent(viewModel: TodoListViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    var darkTheme by remember { mutableStateOf(isDarkTheme) }

    LaunchedEffect(viewModel) {
        viewModel.themeMode.collect { theme ->
            darkTheme = when (theme) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                Theme.SYSTEM -> isDarkTheme
            }
        }
    }

    MyToDoListTheme(darkTheme = darkTheme) {
        MainScreen(viewModel.todoItemsRepository, viewModel.themePreferences)
    }
}
