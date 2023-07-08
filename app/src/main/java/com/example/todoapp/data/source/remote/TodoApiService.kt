package com.example.todoapp.data.source.remote

import com.example.todoapp.data.source.remote.models.ApiItemMessage
import com.example.todoapp.data.source.remote.models.ApiListMessage
import com.example.todoapp.utils.LAST_KNOWN_REVISION_HEADER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface TodoApiService {
    @GET("list")
    suspend fun getTodoItems(): Response<ApiListMessage>

    @PATCH("list")
    suspend fun updateList(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Body apiListMessage: ApiListMessage
    ): Response<ApiListMessage>

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): Response<ApiItemMessage>

    @POST("list")
    suspend fun addTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Body apiItemMessage: ApiItemMessage
    ): Response<ApiItemMessage>

    @PUT("list/{id}")
    suspend fun editTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Path("id") id: String,
        @Body apiItemMessage: ApiItemMessage
    ): Response<ApiItemMessage>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Path("id") id: String
    ): Response<ApiItemMessage>
}

