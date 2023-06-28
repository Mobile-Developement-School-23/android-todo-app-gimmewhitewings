package com.example.todoapp.data.models

import java.util.Date

data class TodoItem(
    val id: String,
    var text: String,
    var importance: Importance,
    var isCompleted: Boolean,
    val createdAt: Date,
    var deadline: Date? = null,
    var modifiedAt: Date? = null
)


