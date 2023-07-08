package com.example.todoapp.di.module

import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.di.scope.AppScope
import com.example.todoapp.utils.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object ApiModule {
    @AppScope
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @AppScope
    @Provides
    fun provideTodoApiService(retrofit: Retrofit): TodoApiService {
        return retrofit.create(TodoApiService::class.java)
    }
}