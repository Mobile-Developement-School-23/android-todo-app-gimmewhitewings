package com.example.todoapp.data.source.remote.models

import com.example.todoapp.data.source.local.room.entity.TodoItemEntity
import com.example.todoapp.data.model.Importance
import com.squareup.moshi.Json

data class TodoItemDto(
    @Json(name = "id") val id: String,
    @Json(name = "text") val text: String,
    @Json(name = "importance") val importance: String,
    @Json(name = "deadline") val deadline: Long?,
    @Json(name = "done") val isCompleted: Boolean,
    @Json(name = "color") val color: String? = "",
    @Json(name = "created_at") val createdAt: Long,
    @Json(name = "changed_at") val modifiedAt: Long?,
    @Json(name = "last_updated_by") val lastUpdatedBy: String = ""
)

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
fun TodoItemDto.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = when (importance) {
        "low" -> Importance.LOW
        "basic" -> Importance.COMMON
        "important" -> Importance.HIGH
        else -> Importance.COMMON
    },
    deadline = deadline,
    isCompleted = isCompleted,
    createdAt = createdAt,
    modifiedAt = modifiedAt
)
