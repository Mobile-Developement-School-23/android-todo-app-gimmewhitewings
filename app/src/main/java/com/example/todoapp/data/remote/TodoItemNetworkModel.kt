package com.example.todoapp.data.remote

import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.google.gson.annotations.SerializedName
import java.util.Date

data class TodoItemNetworkModel(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val isCompleted: Boolean,
    @SerializedName("color") val color: String? = "",
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val modifiedAt: Long?,
    @SerializedName("last_updated_by") val lastUpdatedBy: String
)

fun TodoItemNetworkModel.asExternalModel(): TodoItem {
    return TodoItem(
        id = id,
        text = text,
        importance = Importance.valueOf(importance),
        isCompleted = isCompleted,
        createdAt = Date(createdAt),
        deadline = deadline?.let { Date(it) },
        modifiedAt = modifiedAt?.let { Date(it) }
    )
}

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
fun TodoItemNetworkModel.asEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = when (importance) {
        "low" -> Importance.LOW
        "basic" -> Importance.COMMON
        "high" -> Importance.HIGH
        else -> Importance.COMMON
    },
    deadline = deadline,
    isCompleted = isCompleted,
    createdAt = createdAt,
    modifiedAt = modifiedAt
)