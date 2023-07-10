package com.example.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.source.local.room.dao.TodoItemsDao
import com.example.todoapp.data.source.local.room.db.TodoItemsDatabase
import com.example.todoapp.di.scope.AppScope
import dagger.Module
import dagger.Provides


@Module
object DatabaseModule {

    @AppScope
    @Provides
    fun provideTodoItemsDao(database: TodoItemsDatabase): TodoItemsDao {
        return database.todoItemsDao()
    }

    @AppScope
    @Provides
    fun provideTodoItemsDatabase(context: Context): TodoItemsDatabase {
        return Room.databaseBuilder(
            context,
            TodoItemsDatabase::class.java,
            "todo_items_database"
        ).build()
    }
}