package com.example.mytodolist.presentation.navigation

import AddTaskScreen
import TodoListElement
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytodolist.data.domain.ITodoItemsRepository
import com.example.mytodolist.data.domain.ThemePreferences

@Composable
fun MainScreen(
    todoItemsRepository: ITodoItemsRepository,
    themePreferences: ThemePreferences
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    todoItemsRepository.setSnackbarHostState(snackbarHostState)

    NavHost(navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListElement(
                todoItemsRepository = todoItemsRepository,
                snackbarHostState = snackbarHostState,
                themePreferences = themePreferences,
                navigateToAddTask = { taskId ->
                    if (taskId != "0") {
                        navController.navigate("addTask/$taskId")
                    } else {
                        navController.navigate("addTask/0")
                    }
                }
            )
        }
        composable(
            "addTask/{taskId}",
            arguments = listOf(navArgument("taskId") {
                type = NavType.StringType
                defaultValue = "0"
                nullable = true
            })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            AddTaskScreen(
                todoItemsRepository = todoItemsRepository,
                taskId = taskId,
                navController = navController
            )
        }
    }
}