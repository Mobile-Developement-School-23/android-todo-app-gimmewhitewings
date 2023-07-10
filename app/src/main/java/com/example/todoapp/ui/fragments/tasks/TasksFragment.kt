package com.example.todoapp.ui.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.appComponent
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.ui.fragments.tasks.adapter.TodoItemDiffCalculator
import com.example.todoapp.ui.fragments.tasks.adapter.TodoItemsAdapter
import com.example.todoapp.ui.fragments.tasks.viewmodel.TasksViewModel
import kotlinx.coroutines.launch


class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TodoItemsAdapter

    private val component by lazy { requireActivity().appComponent.tasksFragmentComponent() }

    private val viewModel: TasksViewModel by viewModels {
        TasksViewModel.Factory(
            component.provideTasksViewModel()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initRecyclerView()
        bindUncompletedItemsCheckbox()
        bindAddNewTaskButton()
        bindFab()
    }

    private fun bindFab() {
        binding.floatingActionButton.setOnClickListener {
            addNewTask()
        }
    }

    private fun bindAddNewTaskButton() {
        binding.addNewTaskButton.setOnClickListener {
            addNewTask()
        }
    }

    private fun bindUncompletedItemsCheckbox() {
        binding.showUncompletedItemsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.showUncompletedTodoItems(isChecked)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    Log.d("err", "err: ${it.showError}")
                    showErrorUi(it.showError)
                    adapter.submitList(it.todoItemsList)
                    binding.amountCompletedTextView.text =
                        getString(R.string.done_amount, it.completedTodoItemsNumber)
                }
            }
        }
    }

    private fun showErrorUi(showError: Boolean) {
        binding.errorTextView.isVisible = showError
        binding.refreshButton.isVisible = showError
        binding.errorTextView.text = getString(R.string.not_synchronized)
        binding.refreshButton.setOnClickListener {
            viewModel.updateRepo()
        }
    }

    private fun addNewTask() {
        findNavController().navigate(R.id.addEditTask)
    }

    private fun editTask(todoItemId: String) {
        val action = TasksFragmentDirections.addEditTask(todoItemId)
        findNavController().navigate(action)
    }

    private fun initRecyclerView() {
        adapter = TodoItemsAdapter(viewModel, TodoItemDiffCalculator()) { todoItemId ->
            editTask(todoItemId)
        }
        binding.recyclerView.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = this@TasksFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
