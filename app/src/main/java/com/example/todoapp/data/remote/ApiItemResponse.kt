package com.example.todoapp.data.remote

import com.google.gson.annotations.SerializedName

data class ApiItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("element") val todoItemApiModel: TodoItemApiModel,
    @SerializedName("revision") val revision: Int
)
