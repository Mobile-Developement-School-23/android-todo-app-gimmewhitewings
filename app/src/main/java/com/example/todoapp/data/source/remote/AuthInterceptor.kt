package com.example.todoapp.data.source.remote

import com.example.todoapp.utils.AUTH_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $AUTH_TOKEN")
            .build()
        return chain.proceed(request)
    }
}