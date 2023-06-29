package com.example.todoapp.data.remote

import com.google.gson.annotations.SerializedName

data class ApiListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val todoItemApiModelList: List<TodoItemApiModel>,
    @SerializedName("revision") val revision: Int
)

