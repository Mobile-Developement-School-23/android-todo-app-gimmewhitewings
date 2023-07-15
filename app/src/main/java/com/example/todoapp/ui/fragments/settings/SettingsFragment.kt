package com.example.todoapp.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoapp.appComponent
import com.example.todoapp.ui.fragments.settings.viewmodel.SettingsViewModel
import com.example.todoapp.ui.theme.ApplicationTheme
import com.example.todoapp.ui.theme.TodoAppTheme

class SettingsFragment : Fragment() {

    private val component by lazy { requireActivity().appComponent.settingsFragmentComponent() }

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.Factory(
            component.provideSettingsViewModel()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TodoAppTheme(
                    darkTheme = viewModel.theme() == ApplicationTheme.NIGHT ||
                            (viewModel.theme() == ApplicationTheme.SYSTEM && isSystemInDarkTheme())
                ) {
                    SettingsScreen(viewModel)
                }
            }
        }
    }

    @Composable
    private fun SettingsScreen(viewModel: SettingsViewModel) {
        val viewState = viewModel.uiState.collectAsState()
        Surface(
            modifier = Modifier.background(
                color = TodoAppTheme.colors.backPrimary
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    viewModel.setAppTheme(ApplicationTheme.DAY)
                    activity?.recreate()
                }) {
                    Text(text = "day")
                }
                Button(onClick = {
                    viewModel.setAppTheme(ApplicationTheme.SYSTEM)
                    activity?.recreate()
                }
                ) {
                    Text(text = "system")
                }
                Button(onClick = {
                    viewModel.setAppTheme(ApplicationTheme.NIGHT)
                    activity?.recreate()
                }) {
                    Text(text = "night")
                }
            }
        }
    }
}
