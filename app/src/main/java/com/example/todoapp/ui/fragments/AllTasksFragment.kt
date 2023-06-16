package com.example.todoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAllTasksBinding
import com.example.todoapp.ui.TodoViewModel
import com.example.todoapp.ui.adapters.TodoItemsAdapter
import kotlinx.coroutines.launch
import kotlin.random.Random


class AllTasksFragment : Fragment() {

    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: TodoViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.amountDoneTextView.text = getString(R.string.done_amount, Random.nextInt(5, 10))

        initRecyclerView()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_allTasksFragment_to_taskFragment)
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean = false
        }

        val todoItemsAdapter = TodoItemsAdapter(
            onItemClicked = { todoItem ->
                // TODO: to second screen
            },
            onCheckboxToggle = { todoItem ->
                viewModel.toggleIsDone(todoItem)
            }
        )
        recyclerView.adapter = todoItemsAdapter

        lifecycleScope.launch {
            viewModel.visibleTodoItems.collect {
                todoItemsAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}