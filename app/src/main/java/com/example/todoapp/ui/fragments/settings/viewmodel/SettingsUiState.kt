package com.example.todoapp.ui.fragments.settings.viewmodel

import com.example.todoapp.ui.theme.ApplicationTheme

data class SettingsUiState(
    val chosenMode: ApplicationTheme = ApplicationTheme.SYSTEM
)
