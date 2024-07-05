package com.example.mytodolist.ui.navigation

import AddTaskScreen
import TodoListElement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytodolist.Classes.TodoItemsRepository

@Composable
fun MainScreen(todoItemsRepository: TodoItemsRepository) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListElement(
                todoItemsRepository = todoItemsRepository,
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