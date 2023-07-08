package com.example.todoapp.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.todoapp.ui.activity.viewmodel.ActivityViewModel
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.appComponent
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val component by lazy { appComponent.activityComponent() }

    private val viewModel: ActivityViewModel by viewModels {
        ActivityViewModel.Factory(
            component.provideActivityViewModel()
        )
    }

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorState.collect {
                    if (it.isErrorShown) {
                        Snackbar.make(
                            binding.fragmentContainerView,
                            getString(R.string.network_error),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (application as ToDoApplication).startUploadWorker()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
