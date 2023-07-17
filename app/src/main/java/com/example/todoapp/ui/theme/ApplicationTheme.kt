package com.example.todoapp.ui.theme

enum class ApplicationTheme {
    DAY,
    NIGHT,
    SYSTEM
}

fun Int.toApplicationTheme(): ApplicationTheme {
    return when (this) {
        0 -> ApplicationTheme.DAY
        1 -> ApplicationTheme.NIGHT
        2 -> ApplicationTheme.SYSTEM
        else -> ApplicationTheme.SYSTEM
    }
}
