package com.example.todoapp.di.component

import com.example.todoapp.di.module.ApiModule
import com.example.todoapp.di.module.AppModule
import com.example.todoapp.di.module.DatabaseModule
import com.example.todoapp.di.scope.AppScope
import dagger.Component

@AppScope
@Component(
    modules = [AppModule::class, ApiModule::class, DatabaseModule::class]
)
interface AppComponent {
    fun activityComponent(): ActivityComponent
    fun tasksFragmentComponent(): TasksFragmentComponent
    fun addEditTaskFragmentComponent(): AddEditTaskFragmentComponent
    fun settingsFragmentComponent(): SettingsFragmentComponent
}
