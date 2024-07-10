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
import com.example.mytodolist.data.TodoItemsRepository

@Composable
fun MainScreen(todoItemsRepository: TodoItemsRepository) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    todoItemsRepository.setSnackbarHostState(snackbarHostState)

    NavHost(navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListElement(
                todoItemsRepository = todoItemsRepository,
                snackbarHostState = snackbarHostState,
                navigateToAddTask = { taskId ->
                    if (taskId != null) {
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
                defaultValue = null
                nullable = true
            })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            AddTaskScreen(taskId = taskId, navController = navController)
        }
    }
}