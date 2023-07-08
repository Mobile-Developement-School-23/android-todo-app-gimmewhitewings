package com.example.todoapp.data.model

import com.example.todoapp.data.source.local.room.entity.TodoItemEntity
import com.example.todoapp.data.source.remote.models.TodoItemDto
import java.util.Date

enum class Importance {
    LOW,
    COMMON,
    HIGH
}

data class TodoItem(
    val id: String,
    var text: String,
    var importance: Importance,
    var isCompleted: Boolean,
    val createdAt: Date,
    var deadline: Date? = null,
    var modifiedAt: Date? = null
)

fun TodoItem.toEntity(): TodoItemEntity = TodoItemEntity(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline?.time,
    isCompleted = isCompleted,
    createdAt = createdAt.time,
    modifiedAt = modifiedAt?.time ?: createdAt.time
)

fun TodoItem.toDto(): TodoItemDto {
    return TodoItemDto(
        id = id,
        text = text,
        importance = when (importance) {
            Importance.HIGH -> "important"
            Importance.COMMON -> "basic"
            Importance.LOW -> "low"
        },
        deadline = deadline?.time,
        isCompleted = isCompleted,
        color = null, // Set color value as needed
        createdAt = createdAt.time,
        modifiedAt = modifiedAt?.time ?: Date().time
    )
}



