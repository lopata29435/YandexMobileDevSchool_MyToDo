package com.example.mytodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.mytodolist.data.domain.Theme
import com.example.mytodolist.data.domain.ThemePreferences
import com.example.mytodolist.presentation.navigation.MainScreen
import com.example.mytodolist.presentation.theme.MyToDoListTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val themePreferences = ThemePreferences(this)
        val themeMode = themePreferences.getThemeMode()
        val todoItemsRepository = (application as App).appComponent.getTodoItemsRepository()

        setContent {
            MyToDoListTheme(darkTheme = when(themeMode) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                Theme.SYSTEM -> isSystemInDarkTheme()
            }) {
                MainScreen(todoItemsRepository, themePreferences)
            }
        }
    }
}


