package com.example.todoapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.data.local.dao.TodoItemsDao
import com.example.todoapp.data.local.entity.TodoItemEntity


@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoItemsDatabase : RoomDatabase() {
    abstract fun todoItemsDao(): TodoItemsDao

    companion object {
        @Volatile
        private var INSTANCE: TodoItemsDatabase? = null
        fun getDatabase(context: Context): TodoItemsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE
                if (instance == null) {
                    val newInstance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoItemsDatabase::class.java,
                        "todo_items_database"
                    ).build()
                    INSTANCE = newInstance
                    newInstance
                } else {
                    instance
                }
            }
        }
    }
}
