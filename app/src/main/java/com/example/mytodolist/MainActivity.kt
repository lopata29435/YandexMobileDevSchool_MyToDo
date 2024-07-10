package com.example.mytodolist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodolist.presentation.navigation.MainScreen
import com.example.mytodolist.presentation.theme.MyToDoListTheme

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


