package com.example.todoapp.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.todoapp.data.source.local.room.entity.TodoItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Todo items dao - room data access object
 *
 * @constructor Create empty Todo items dao
 */
@Dao
interface TodoItemsDao {

    @Query("SELECT * FROM todo_item ORDER BY created_at DESC")
    fun getAllTodoItemsStream(): Flow<List<TodoItemEntity>>

    @Query("SELECT * FROM todo_item ORDER BY created_at DESC")
    suspend fun getAllTodoItemsSnapshot(): List<TodoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(newTodoItemEntity: TodoItemEntity)

    @Query("SELECT * FROM todo_item WHERE id = :itemId")
    suspend fun getTodoItemById(itemId: String): TodoItemEntity?

    @Query("DELETE FROM todo_item WHERE id = :itemId")
    suspend fun deleteTodoItemById(itemId: String)

    @Update
    suspend fun updateTodoItem(todoItemEntity: TodoItemEntity)

    @Upsert
    suspend fun upsertTodoItem(todoItemEntity: TodoItemEntity)

    @Upsert
    suspend fun upsertTodoItems(todoItemEntities: List<TodoItemEntity>)

    @Delete
    suspend fun deleteTodoItems(todoItemEntities: List<TodoItemEntity>)

    @Update
    suspend fun updateTodoItems(todoItemEntities: List<TodoItemEntity>)

    @Query("UPDATE todo_item SET is_completed = NOT is_completed WHERE id = :todoItemId")
    suspend fun toggleTodoItemCompletionById(todoItemId: String)
}
