package com.example.todoapp.di.component

import com.example.todoapp.di.scope.TasksScope
import com.example.todoapp.ui.fragments.tasks.viewmodel.TasksViewModel
import dagger.Subcomponent


@Subcomponent
@TasksScope
interface TasksFragmentComponent {

    @TasksScope
    fun provideTasksViewModel(): TasksViewModel.TasksViewModelFactory
}
