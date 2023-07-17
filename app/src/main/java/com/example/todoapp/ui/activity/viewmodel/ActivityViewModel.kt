package com.example.todoapp.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.data.source.local.SharedPreferencesManager
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivityViewModel @AssistedInject constructor(
    repository: TodoItemsRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    @AssistedFactory
    interface ActivityViewModelFactory {
        fun create(): ActivityViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: ActivityViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }


    private var _errorState = MutableStateFlow(ErrorUiState())
    val errorState = _errorState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.errorFlow.collect { result ->
                _errorState.update {
                    it.copy(
                        isErrorShown = result.isFailure,
                        error = result.exceptionOrNull()
                    )
                }
            }
        }
    }

    fun theme() = sharedPreferencesManager.getApplicationTheme()
}

