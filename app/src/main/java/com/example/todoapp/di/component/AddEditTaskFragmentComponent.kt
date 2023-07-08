package com.example.todoapp.di.component

import com.example.todoapp.di.scope.AddEditScope
import com.example.todoapp.ui.AddEditTaskViewModel
import dagger.Subcomponent

@Subcomponent
@AddEditScope
interface AddEditTaskFragmentComponent {

    @AddEditScope
    fun provideAddEditTaskViewModel(): AddEditTaskViewModel.AddEditTasksViewModelFactory
}