package com.example.mytodolist.ui.navigation

import AddTaskScreen
import TodoListElement
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytodolist.Classes.TodoItemsRepository
import com.example.mytodolist.R

@Composable
fun MainScreen(todoItemsRepository: TodoItemsRepository) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.snack_error)
    val retryActionLabel = stringResource(id = R.string.snack_action)

    NavHost(navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListElement(
                todoItemsRepository = todoItemsRepository,
                snackbarHostState = snackbarHostState,
                errorMessage = errorMessage,
                retryActionLabel = retryActionLabel,
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