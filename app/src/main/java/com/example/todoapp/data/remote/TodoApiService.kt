package com.example.todoapp.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
private const val AUTH_TOKEN = "calligraphic"
private const val LAST_KNOWN_REVISION_HEADER = "X-Last-Known-Revision"

private val gson = Gson()

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor(AUTH_TOKEN))
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()


interface TodoApiService {
    @GET("list")
    suspend fun getTodoItems(): Response<ApiListResponse>

    @PATCH("list")
    suspend fun updateList(@Header(LAST_KNOWN_REVISION_HEADER) revision: Int)

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): Response<ApiItemResponse>

    @POST("list")
    suspend fun addTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        todoItemNetworkModel: TodoItemNetworkModel
    ): Response<ApiItemResponse>

    @PUT("list/{id}")
    suspend fun editTodoItem(
        @Path("id") id: String,
        todoItemNetworkModel: TodoItemNetworkModel
    ): Response<ApiItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id") id: String
    ): Response<ApiItemResponse>
}

object TodoApi {
    val retrofitService: TodoApiService by lazy { retrofit.create(TodoApiService::class.java) }
}