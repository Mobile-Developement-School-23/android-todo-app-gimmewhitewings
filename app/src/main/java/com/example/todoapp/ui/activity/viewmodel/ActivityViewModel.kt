package com.example.todoapp.ui.activity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.repository.TodoItemsRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivityViewModel @AssistedInject constructor(
    repository: TodoItemsRepository
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
                Log.e("dddd", "ddd: ${result.exceptionOrNull()?.message}")
                _errorState.update {
                    it.copy(
                        isErrorShown = result.isFailure,
                        error = result.exceptionOrNull()
                    )
                }
            }
        }
    }
}

