package com.example.todoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.Importance
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.TodoViewModel
import com.example.todoapp.utils.resolveColorAttribute
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null

    private val binding get() = _binding!!

    private val viewModel: TodoViewModel by activityViewModels()

    private lateinit var itemId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemId = it.getString("todoItemId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getTodoItemById(itemId)
                viewModel.selectedTodoItem.collect { todoItem ->
                    if (todoItem != null) {
                        binding.apply {
                            todoTextEditText.setText(todoItem.text)

                            importanceTextView.text = when (todoItem.importance) {
                                Importance.LOW -> getString(R.string.low)
                                Importance.COMMON -> getString(R.string.no)
                                Importance.HIGH -> getString(R.string.high)
                            }

                            deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    deadlineTextView.setTextColor(
                                        requireContext().resolveColorAttribute(
                                            R.attr.color_blue
                                        )
                                    )
                                    val pattern = "dd MMM yyyy"
                                    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                                    val dateText =
                                        todoItem.deadline?.let { date -> formatter.format(date) }
                                    deadlineTextView.text = dateText
                                } else {
                                    deadlineTextView.text = getString(R.string.no)
                                    deadlineTextView.setTextColor(
                                        requireContext().resolveColorAttribute(
                                            R.attr.label_tertiary
                                        )
                                    )
                                }
                            }
                            deadlineSwitch.isChecked = todoItem.deadline != null
                        }
                    }
                }
            }
        }

        binding.apply {
            saveButton.setOnClickListener {
                findNavController().popBackStack()
            }

            closeButton.setOnClickListener {
                findNavController().popBackStack()
            }

            deleteButton.setOnClickListener {
                viewModel.deleteTodoItemById(itemId)
                findNavController().popBackStack()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}