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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SettingsScreen(viewModel: SettingsViewModel) {
        val viewState = viewModel.uiState.collectAsState()
        val onButtonClick: (ApplicationTheme) -> Unit = {
            viewModel.setAppTheme(it)
            activity?.recreate()
        }
        val onCloseButtonClick: () -> Unit = {
            findNavController().popBackStack()
        }
        Surface(
            modifier = Modifier.background(color = TodoAppTheme.colors.backPrimary)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = TodoAppTheme.colors.backPrimary
                        ),
                        navigationIcon = {
                            IconButton(onClick = onCloseButtonClick) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "close",
                                    tint = TodoAppTheme.colors.labelPrimary
                                )
                            }
                        },
                        title = {}
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize()
                        .background(color = TodoAppTheme.colors.backPrimary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    listOf(
                        ApplicationTheme.DAY,
                        ApplicationTheme.SYSTEM,
                        ApplicationTheme.NIGHT
                    ).forEach { applicationTheme ->
                        ApplicationThemeButton(
                            isSelected = applicationTheme == viewState.value.chosenMode,
                            onButtonClick = onButtonClick,
                            applicationTheme = applicationTheme
                        )
                    }
                }
            }

        }
    }

    @Composable
    fun ApplicationThemeButton(
        onButtonClick: (ApplicationTheme) -> Unit,
        applicationTheme: ApplicationTheme,
        isSelected: Boolean
    ) {
        Button(
            onClick = { onButtonClick(applicationTheme) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TodoAppTheme.colors.colorBlue.copy(
                    alpha = if (isSelected) 1f else 0.2f
                ),
                contentColor = Color.White
            )
        ) {
            Icon(
                painter = applicationTheme.toPainterResource(),
                contentDescription = applicationTheme.toStringResource()
            )
            Text(text = applicationTheme.toStringResource())
        }
    }
}

@Composable
fun ApplicationTheme.toStringResource(): String {
    return when (this) {
        ApplicationTheme.DAY -> stringResource(id = R.string.always_day)
        ApplicationTheme.NIGHT -> stringResource(id = R.string.always_night)
        ApplicationTheme.SYSTEM -> stringResource(id = R.string.system_them)
    }
}

@Composable
fun ApplicationTheme.toPainterResource(): Painter {
    return painterResource(
        id = when (this) {
            ApplicationTheme.DAY -> R.drawable.ic_day
            ApplicationTheme.NIGHT -> R.drawable.ic_night
            ApplicationTheme.SYSTEM -> R.drawable.ic_system_theme
        }
    )
}
