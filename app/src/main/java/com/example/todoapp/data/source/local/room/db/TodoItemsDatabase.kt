package com.example.todoapp.data.source.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.source.local.room.dao.TodoItemsDao
import com.example.todoapp.data.source.local.room.entity.TodoItemEntity

/**
 * Todo items database
 *
 * @constructor Create empty Todo items database
 */
@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoItemsDatabase : RoomDatabase() {
    abstract fun todoItemsDao(): TodoItemsDao
}
