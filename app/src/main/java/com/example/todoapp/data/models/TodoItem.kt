package com.example.todoapp.data.models

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    var isCompleted: Boolean,
    val createdAt: Date,
    var deadline: Date? = null,
    var modifiedAt: Date? = null
)


