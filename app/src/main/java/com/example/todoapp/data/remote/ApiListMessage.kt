package com.example.todoapp.data.remote

import com.squareup.moshi.Json

data class ApiListMessage(
    @Json(name = "status") val status: String? = null,
    @Json(name = "list") val todoItemNetworkModelList: List<TodoItemNetworkModel>,
    @Json(name = "revision") val revision: Int? = null
)

