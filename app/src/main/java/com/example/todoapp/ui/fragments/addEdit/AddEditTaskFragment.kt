package com.example.todoapp.ui.fragments.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.appComponent
import com.example.todoapp.data.model.Importance
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.example.todoapp.ui.fragments.addEdit.viewmodel.AddEditTaskViewModel
import com.example.todoapp.utils.DateFormatter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.util.Date


class AddEditTaskFragment : Fragment() {

    private var _binding: FragmentAddEditTaskBinding? = null

    private val binding get() = _binding!!
    private val component by lazy { requireActivity().appComponent.addEditTaskFragmentComponent() }

    private val viewModel: AddEditTaskViewModel by viewModels {
        AddEditTaskViewModel.Factory(
            component.provideAddEditTaskViewModel()
        )
    }

    private lateinit var datePicker: MaterialDatePicker<Long>
    private var todoItemId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            todoItemId = bundle.getString("todoItemId")
            if (todoItemId != null) {
                viewModel.loadTodoItem(todoItemId!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        bindImportancePopupMenu()
        bindDeleteButton()
        bindSaveButton()
        bindCloseButton()
        bindEditText()
        initDatePicker()
        bindDeadlineSwitch()
    }

    private fun bindDeadlineSwitch() {
        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setDate(Date())
                binding.deadLineButton.apply {
                    isFocusable = true
                    isClickable = true
                    setOnClickListener {
                        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
                    }
                }
            } else {
                binding.deadLineButton.apply {
                    isFocusable = false
                    isClickable = false
                }
                viewModel.setDate(null)
            }
        }
    }

    private fun bindEditText() {
        binding.todoTextEditText.addTextChangedListener {
            viewModel.setText(it.toString())
        }
    }

    private fun bindCloseButton() {
        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindSaveButton() {
        binding.saveButton.setOnClickListener {
            viewModel.saveTodoItem(todoItemId)
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    setUpText(it.text)
                    setUpImportance(it.importance)
                    setUpDeadline(it.deadline)
                }
            }
        }
    }

    private fun setUpText(text: String) {
        binding.todoTextEditText.apply {
            if (!this.isFocused) {
                this.setText(text)
            }
        }
    }

    private fun bindDeleteButton() {
        binding.deleteButton.apply {
            isVisible = todoItemId != null
            setOnClickListener {
                viewModel.deleteTodoItem(todoItemId!!)
                findNavController().popBackStack()
            }
        }
    }

    private fun initDatePicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.deadline))
                .setPositiveButtonText(getString(R.string.save))
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(Date(it))
        }
    }

    private fun bindImportancePopupMenu() {
        binding.importanceButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.importanceButton)
            popupMenu.menuInflater.inflate(R.menu.importance_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                val importance: Importance = when (menuItem.itemId) {
                    R.id.high -> Importance.HIGH
                    R.id.common -> Importance.COMMON
                    R.id.low -> Importance.LOW
                    else -> Importance.COMMON
                }
                viewModel.setImportance(importance)
                true
            }
            popupMenu.show()
        }
    }

    private fun setUpDeadline(deadline: Date?) {
        binding.apply {
            if (deadline != null) {
                deadlineSwitch.isChecked = true
                deadlineTextView.text = DateFormatter.formatter.format(deadline)
            } else {
                deadlineSwitch.isChecked = false
                deadlineTextView.text = ""
            }
        }
    }

    private fun setUpImportance(importance: Importance) {
        binding.importanceTextView.text = when (importance) {
            Importance.LOW -> getString(R.string.low)
            Importance.COMMON -> getString(R.string.no)
            Importance.HIGH -> getString(R.string.high)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
