package com.example.todoapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.remote.models.TodoItemDto
import java.util.Date

@Entity(tableName = "todo_item")
data class TodoItemEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "importance") val importance: Importance,
    @ColumnInfo(name = "deadline") val deadline: Long? = null,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "modified_at") val modifiedAt: Long? = null
)


fun TodoItemEntity.toDto(): TodoItemDto = TodoItemDto(
    id = id,
    text = text,
    importance = when (importance) {
        Importance.LOW -> "low"
        Importance.COMMON -> "basic"
        Importance.HIGH -> "important"
    },
    deadline = deadline,
    isCompleted = isCompleted,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    lastUpdatedBy = "test"
)

/**
 * Converts the local model to the external model for use
 * by layers external to the data layer
 */
fun TodoItemEntity.toDomain(): TodoItem = TodoItem(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline?.let { Date(it) },
    isCompleted = isCompleted,
    createdAt = Date(createdAt),
    modifiedAt = modifiedAt?.let { Date(it) }
)



