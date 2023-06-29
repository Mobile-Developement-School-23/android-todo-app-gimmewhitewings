package com.example.todoapp.data.remote

import com.google.gson.annotations.SerializedName

data class ApiListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val todoItemNetworkModelList: List<TodoItemNetworkModel>,
    @SerializedName("revision") val revision: Int
)

