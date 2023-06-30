package com.example.todoapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.data.SharedPreferencesManager
import com.example.todoapp.data.local.db.TodoItemsDatabase
import com.example.todoapp.data.remote.AuthInterceptor
import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.utils.AUTH_TOKEN
import com.example.todoapp.utils.BASE_URL
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ToDoApplication : Application() {
    private val moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(AUTH_TOKEN))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: TodoApiService by lazy { retrofit.create(TodoApiService::class.java) }
    private val db by lazy { TodoItemsDatabase.getDatabase(this) }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(
            "todo_app_shared_preferences",
            Context.MODE_PRIVATE
        )
    }

    val repository: TodoItemsRepository by lazy {
        TodoItemsRepository(
            db.todoItemsDao(),
            retrofitService,
            SharedPreferencesManager(sharedPreferences)
        )
    }
}