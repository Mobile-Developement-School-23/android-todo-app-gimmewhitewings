package com.example.todoapp.ui.fragments.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    AddEditTaskScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AddEditTaskScreen() {
        Scaffold(
            modifier = Modifier.background(color = Color(0xFFF7F6F2)),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close"
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.save))
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(16.dp)
                ) {
                    DescriptionCard()

                    Spacer(modifier = Modifier.height(12.dp))

                    ImportanceSection()

                    Divider()

                    DeadlineSection()
                    Spacer(Modifier.height(24.dp))
                    Divider()
                    TextButton(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                        onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete"
                        )
                        Text(text = stringResource(id = R.string.delete))
                    }
                }
            }
        )
    }

    @Composable
    private fun DeadlineSection() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var checked by remember { mutableStateOf(true) }
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = stringResource(id = R.string.deadline))
                Text(text = "12 июл 2023", Modifier.padding(top = 4.dp))
            }
            Switch(
                checked = checked,
                onCheckedChange = { checked = !checked }
            )
        }
    }

    @Composable
    private fun ImportanceSection() {
        Column(
            modifier = Modifier
                .clickable {}
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.priority))
            Text(text = "Нет", Modifier.padding(top = 4.dp))
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewImportanceSection() {
        MaterialTheme {
            ImportanceSection()
        }
    }

    @Composable
    private fun DescriptionCard() {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BasicTextField(
                value = "",
                modifier = Modifier
                    .padding(16.dp)
                    .defaultMinSize(minHeight = 104.dp)
                    .fillMaxWidth(),
                onValueChange = { }
            ) {
                Text(text = stringResource(id = R.string.enter_task))
            }
        }
    }

    @Preview
    @Composable
    fun PreviewDescriptionCard() {
        MaterialTheme {
            DescriptionCard()
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun ScreenPreview() {
        MaterialTheme {
            AddEditTaskScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        observeViewModel()
//
//        bindImportancePopupMenu()
//        bindDeleteButton()
//        bindSaveButton()
//        bindCloseButton()
//        bindEditText()
//        initDatePicker()
//        bindDeadlineSwitch()
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
