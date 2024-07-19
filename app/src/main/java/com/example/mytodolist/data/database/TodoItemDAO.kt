package com.example.mytodolist.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mytodolist.data.domain.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE id = :itemId")
    fun getById(itemId: String): TodoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todoItems: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoItem: TodoItem)

    @Update
    suspend fun update(todoItem: TodoItem)

    @Query("DELETE FROM todo_items WHERE id = :todoItemId")
    suspend fun delete(todoItemId: String)
}