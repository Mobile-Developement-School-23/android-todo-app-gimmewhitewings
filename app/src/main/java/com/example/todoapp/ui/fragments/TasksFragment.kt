package com.example.todoapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.ui.TasksViewModel
import com.example.todoapp.ui.adapters.TodoItemDiffCalculator
import com.example.todoapp.ui.adapters.TodoItemsAdapter
import kotlinx.coroutines.launch


class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TodoItemsAdapter

    private val viewModel: TasksViewModel by viewModels { TasksViewModel.Factory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        initRecyclerView()

        binding.showUncompletedItemsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.showUncompletedTodoItems(isChecked)
        }

        binding.addNewTaskButton.setOnClickListener {
            addNewTask()
        }

        binding.floatingActionButton.setOnClickListener {
            addNewTask()
        }
    }

    private fun showErrorUi(showError: Boolean) {
        binding.errorTextView.visibility = if (showError) View.VISIBLE else View.GONE
        binding.refreshButton.visibility = if (showError) View.VISIBLE else View.GONE
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