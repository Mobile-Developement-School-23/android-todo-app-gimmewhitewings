package com.example.todoapp.ui.fragments.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.source.local.SharedPreferencesManager
import com.example.todoapp.ui.theme.ApplicationTheme
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel @AssistedInject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    fun setAppTheme(appTheme: ApplicationTheme) {
        sharedPreferencesManager.setApplicationTheme(appTheme)
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    chosenMode = appTheme
                )
            }
        }
    }

    private var _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val theme = theme()
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    chosenMode = theme
                )
            }
        }
    }

    fun theme() = sharedPreferencesManager.getApplicationTheme()

    @AssistedFactory
    interface SettingsViewModelFactory {
        fun create(): SettingsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: SettingsViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

}
