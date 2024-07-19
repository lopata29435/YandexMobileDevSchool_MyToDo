package com.example.mytodolist.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.mytodolist.AddTaskViewModelFactory
import com.example.mytodolist.data.database.AppDatabase
import com.example.mytodolist.data.database.TodoItemDao
import com.example.mytodolist.data.domain.ITodoItemsRepository
import com.example.mytodolist.data.domain.TodoItemsRepositoryImpl
import com.example.mytodolist.presentation.viewmodels.TodoListViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    @Provides
    @Singleton
    fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
        return appDatabase.todoItemDao()
    }
}

@Module
abstract class RepositoryModule {
}

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("MyToDoPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideTodoItemsRepository(
        database: AppDatabase,
        todoItemDao: TodoItemDao,
        sharedPreferences: SharedPreferences
    ): ITodoItemsRepository {
        return TodoItemsRepositoryImpl(database, todoItemDao, sharedPreferences)
    }

    @Provides
    fun provideAddTaskViewModelFactory(
        repository: ITodoItemsRepository,
        taskId: String?
    ): AddTaskViewModelFactory {
        return AddTaskViewModelFactory(repository, taskId)
    }

    @Provides
    fun provideTodoListViewModelFactory(
        repository: ITodoItemsRepository
    ): TodoListViewModelFactory {
        return TodoListViewModelFactory(repository)
    }
}

@Module
class ActivityModule {
    @Provides
    fun provideAddTaskViewModelFactory(
        repository: ITodoItemsRepository,
        taskId: String?
    ): AddTaskViewModelFactory {
        return AddTaskViewModelFactory(repository, taskId)
    }

    @Provides
    fun provideTodoListViewModelFactory(
        repository: ITodoItemsRepository
    ): TodoListViewModelFactory {
        return TodoListViewModelFactory(repository)
    }
}
