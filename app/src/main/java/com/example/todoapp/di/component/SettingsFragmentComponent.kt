package com.example.todoapp.di.component

import com.example.todoapp.di.scope.SettingsScope
import com.example.todoapp.ui.fragments.settings.viewmodel.SettingsViewModel
import dagger.Subcomponent

@Subcomponent
@SettingsScope
interface SettingsFragmentComponent {

    @SettingsScope
    fun provideSettingsViewModel(): SettingsViewModel.SettingsViewModelFactory
}
