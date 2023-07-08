package com.example.todoapp.data.source.remote.models

import com.squareup.moshi.Json

data class ApiItemMessage(
    @Json(name = "status") val status: String? = null,
    @Json(name = "element") val todoItemNetworkModel: TodoItemDto? = null,
    @Json(name = "revision") val revision: Int? = null
)
