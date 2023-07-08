package com.example.todoapp.di.component

import com.example.todoapp.ActivityViewModel
import com.example.todoapp.di.scope.ActivityScope
import dagger.Subcomponent


@Subcomponent
@ActivityScope
interface ActivityComponent {
    @ActivityScope
    fun provideActivityViewModel(): ActivityViewModel.ActivityViewModelFactory
}