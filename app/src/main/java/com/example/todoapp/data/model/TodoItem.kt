package com.example.todoapp.data.model

import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.remote.TodoItemNetworkModel
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

fun TodoItem.asEntity(): TodoItemEntity = TodoItemEntity(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline?.time,
    isCompleted = isCompleted,
    createdAt = createdAt.time,
    modifiedAt = modifiedAt?.time
)

fun TodoItem.asNetworkModel(): TodoItemNetworkModel {
    return TodoItemNetworkModel(
        id = id,
        text = text,
        importance = importance.name,
        deadline = deadline?.time,
        isCompleted = isCompleted,
        color = null, // Set color value as needed
        createdAt = createdAt.time,
        modifiedAt = modifiedAt?.time,
        lastUpdatedBy = ""
    )
}



