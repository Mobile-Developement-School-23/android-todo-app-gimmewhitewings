package com.example.todoapp.ui.fragments.addEdit.viewmodel

import com.example.todoapp.data.model.Importance
import java.util.Date

data class AddEditUiState(
    val text: String = "",
    val importance: Importance = Importance.COMMON,
    val deadline: Date? = null,
)
