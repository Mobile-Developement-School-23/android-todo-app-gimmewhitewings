package com.example.todoapp.data.remote

import com.google.gson.annotations.SerializedName

data class ApiItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("element") val todoItemNetworkModel: TodoItemNetworkModel,
    @SerializedName("revision") val revision: Int
)
