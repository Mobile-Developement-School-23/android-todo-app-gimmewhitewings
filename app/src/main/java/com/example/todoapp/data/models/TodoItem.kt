package com.example.todoapp.data.models

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val priority: Priority,
    var isDone: Boolean,
    val dateCreated: Date,
    var dateDeadline: Date? = null,
    var dateEdited: Date? = null
)


