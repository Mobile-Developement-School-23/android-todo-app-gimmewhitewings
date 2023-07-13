package com.example.todoapp.ui.fragments.addEdit

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.appComponent
import com.example.todoapp.data.model.Importance
import com.example.todoapp.ui.fragments.addEdit.viewmodel.AddEditTaskViewModel
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.utils.DateFormatter
import kotlinx.coroutines.launch
import java.util.Date


class AddEditTaskFragment : Fragment() {
    private val component by lazy { requireActivity().appComponent.addEditTaskFragmentComponent() }

    private val viewModel: AddEditTaskViewModel by viewModels {
        AddEditTaskViewModel.Factory(
            component.provideAddEditTaskViewModel()
        )
    }

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
                TodoAppTheme {
                    AddEditTaskScreen(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AddEditTaskScreen(
        addEditTaskViewModel: AddEditTaskViewModel = viewModel()
    ) {
        val addEditTaskUiState = addEditTaskViewModel.uiState.collectAsState()
        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val onCloseButtonClicked: () -> Unit = {
            findNavController().popBackStack()
        }
        val onSaveButtonClicked: () -> Unit = {
            addEditTaskViewModel.saveTodoItem(todoItemId)
            findNavController().popBackStack()
        }
        Scaffold(
            containerColor = TodoAppTheme.colors.backPrimary,
            topBar = {
                AddEditTaskTopAppBar(onCloseButtonClicked, onSaveButtonClicked)
            },
            content = { innerPadding ->
                Column(
                    Modifier
                        .background(color = TodoAppTheme.colors.backPrimary)
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(16.dp)
                ) {
                    DescriptionCard(
                        descriptionText = addEditTaskUiState.value.text,
                        onDescriptionTextChanged = { viewModel.setText(it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ImportanceSection(
                        importance = addEditTaskUiState.value.importance,
                        onSectionClicked = { showBottomSheet = true }
                    )

                    Divider()

                    var openDialog by remember { mutableStateOf(false) }
                    val onDateSaved: (Long) -> Unit = {
                        addEditTaskViewModel.setDate(Date(it))
                        openDialog = false
                    }
                    if (openDialog) {
                        val datePickerState = rememberDatePickerState()
                        DatePickerDialog(
                            onDismissRequest = { openDialog = false },
                            colors = DatePickerDefaults.colors(
                                containerColor = TodoAppTheme.colors.backSecondary,

                                ),
                            confirmButton = {
                                TextButton(
                                    enabled = datePickerState.selectedDateMillis != null,
                                    onClick = { onDateSaved(datePickerState.selectedDateMillis!!) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = TodoAppTheme.colors.colorBlue,
                                        disabledContainerColor = Color.Transparent,
                                        disabledContentColor = TodoAppTheme.colors.labelDisable
                                    )
                                ) {
                                    Text(text = stringResource(id = R.string.save).uppercase())
                                }
                            },
                            modifier = Modifier.padding(horizontal = 100.dp)
                        ) {
                            DatePicker(
                                state = datePickerState,
                                colors = DatePickerDefaults.colors(
                                    selectedDayContentColor = TodoAppTheme.colors.labelPrimary,
                                    selectedDayContainerColor = TodoAppTheme.colors.colorBlue,
                                    todayContentColor = TodoAppTheme.colors.colorBlue,
                                    todayDateBorderColor = TodoAppTheme.colors.colorBlue
                                )
                            )
                        }
                    }
                    val deadlineDate = addEditTaskUiState.value.deadline
                    DeadlineSection(
                        deadlineDate = deadlineDate,
                        onSectionClicked = { openDialog = true },
                        onSwitchCheckedChange = {
                            if (it) addEditTaskViewModel.setDate(Date())
                            if (!it) addEditTaskViewModel.setDate(null)
                            //openDialog = it
                        }
                    )

                    Spacer(Modifier.height(24.dp))
                    Divider()
                    val onDeleteButtonClicked: () -> Unit = {
                        todoItemId?.let { addEditTaskViewModel.deleteTodoItem(it) }
                        findNavController().popBackStack()
                    }
                    //DeleteButton(onDeleteButtonClicked)
                    TextButton(
                        onClick = onDeleteButtonClicked,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = todoItemId != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = TodoAppTheme.colors.colorRed,
                            disabledContentColor = TodoAppTheme.colors.labelDisable,
                            disabledContainerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete, contentDescription = stringResource(
                                id = R.string.delete
                            )
                        )
                        Text(text = stringResource(id = R.string.delete))
                    }

                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            containerColor = TodoAppTheme.colors.backSecondary,
                            sheetState = sheetState
                            //modifier = Modifier.padding(vertical = 64.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 32.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.choose_importance),
                                    style = TodoAppTheme.typography.title,
                                    color = TodoAppTheme.colors.labelPrimary,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )
                                val radioOptions = listOf(
                                    Importance.HIGH,
                                    Importance.COMMON,
                                    Importance.LOW
                                )
                                val initValue = addEditTaskUiState.value.importance
                                val (selectedOption, onOptionSelected) = remember {
                                    mutableStateOf(initValue)
                                }
                                ImportanceRadioButtonGroup(
                                    radioOptions = radioOptions,
                                    selectedOption = selectedOption,
                                    onOptionSelected = onOptionSelected
                                )
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch { sheetState.hide() }
                                            .invokeOnCompletion {
                                                showBottomSheet = false
                                            }
                                        addEditTaskViewModel.setImportance(selectedOption)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = TodoAppTheme.colors.colorBlue
                                    )
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.save).uppercase(),
                                        style = TodoAppTheme.typography.button
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun AddEditTaskTopAppBar(
        onCloseButtonClicked: () -> Unit,
        onSaveButtonClicked: () -> Unit
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TodoAppTheme.colors.backPrimary
            ),
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = onCloseButtonClicked
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close",
                        tint = TodoAppTheme.colors.labelPrimary
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = onSaveButtonClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = TodoAppTheme.colors.colorBlue
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.save).uppercase(),
                        style = TodoAppTheme.typography.button,
                    )
                }
            }
        )
    }

    @Composable
    private fun ImportanceRadioButtonGroup(
        radioOptions: List<Importance>,
        selectedOption: Importance,
        onOptionSelected: (Importance) -> Unit
    ) {
        Column(Modifier.selectableGroup()) {
            radioOptions.forEach { importance ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (importance == selectedOption),
                            onClick = { onOptionSelected(importance) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (importance == selectedOption),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = TodoAppTheme.colors.colorBlue
                        ),
                        onClick = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = importance.toStringResource(),
                        style = TodoAppTheme.typography.body,
                        color = TodoAppTheme.colors.labelPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun DeadlineSection(
        deadlineDate: Date?,
        onSwitchCheckedChange: (Boolean) -> Unit,
        onSectionClicked: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = deadlineDate != null,
                    onClick = onSectionClicked,
                    role = Role.Button
                )
                .padding(16.dp)
                .animateContentSize(
                    animationSpec = tween()
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(id = R.string.deadline),
                    color = TodoAppTheme.colors.labelPrimary,
                    style = TodoAppTheme.typography.body
                )
                if (deadlineDate != null) {
                    Text(
                        text = DateFormatter.formatter.format(deadlineDate),
                        modifier = Modifier.padding(top = 4.dp),
                        color = TodoAppTheme.colors.colorBlue,
                        style = TodoAppTheme.typography.subhead
                    )
                }
            }
            Switch(
                checked = deadlineDate != null,
                onCheckedChange = onSwitchCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TodoAppTheme.colors.colorWhite,
                    checkedTrackColor = TodoAppTheme.colors.colorBlue,
                    uncheckedTrackColor = TodoAppTheme.colors.backPrimary
                )
            )
        }
    }

    @Preview(showBackground = true)
    @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "night")
    @Composable
    fun PreviewDeadlineSection() {
        TodoAppTheme {
            DeadlineSection(
                deadlineDate = Date(),
                onSwitchCheckedChange = {}) {}
        }
    }

    @Preview(showBackground = true)
    @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "night")
    @Composable
    fun PreviewDeadlineSection_noDeadline() {
        TodoAppTheme {
            DeadlineSection(
                deadlineDate = null,
                onSwitchCheckedChange = {}) { }
        }
    }

    @Composable
    private fun ImportanceSection(
        importance: Importance,
        onSectionClicked: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .clickable { onSectionClicked() }
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.priority),
                style = TodoAppTheme.typography.body,
                color = TodoAppTheme.colors.labelPrimary
            )
            Text(
                text = importance.toStringResource(),
                modifier = Modifier.padding(top = 4.dp),
                style = TodoAppTheme.typography.subhead,
                color = TodoAppTheme.colors.labelTertiary
            )
        }
    }

    @Preview(showBackground = true)
    @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "night")
    @Composable
    fun PreviewImportanceSection() {
        TodoAppTheme {
            ImportanceSection(Importance.COMMON) {}
        }
    }

    @Composable
    private fun DescriptionCard(
        descriptionText: String,
        onDescriptionTextChanged: (String) -> Unit
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = TodoAppTheme.colors.backSecondary),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp
            )
        ) {
            BasicTextField(
                value = descriptionText,
                onValueChange = onDescriptionTextChanged,
                cursorBrush = SolidColor(TodoAppTheme.colors.colorBlue),
                modifier = Modifier
                    .padding(16.dp)
                    .defaultMinSize(minHeight = 104.dp)
                    .fillMaxWidth(),
                textStyle = TodoAppTheme.typography.body.copy(
                    color = TodoAppTheme.colors.labelPrimary
                ),
            ) {
                if (descriptionText.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.enter_task),
                        style = TodoAppTheme.typography.body,
                        color = TodoAppTheme.colors.labelTertiary
                    )
                }
                it.invoke()
            }
        }
    }

    @Preview(showBackground = true)
    @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "night")
    @Composable
    fun PreviewDescriptionCard() {
        TodoAppTheme {
            DescriptionCard(descriptionText = "Lorem ipsum") {}
        }
    }

    @Preview(showBackground = true)
    @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "night")
    @Composable
    fun PreviewDescriptionCard_noDescription() {
        TodoAppTheme {
            DescriptionCard(descriptionText = "") {}
        }
    }

    @Preview(showBackground = true)
    @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "night")
    @Composable
    fun PreviewTopAppBar() {
        TodoAppTheme {
            AddEditTaskTopAppBar({}, {})
        }
    }

    @Composable
    fun Importance.toStringResource(): String = when (this) {
        Importance.HIGH -> stringResource(id = R.string.high)
        Importance.LOW -> stringResource(id = R.string.low)
        Importance.COMMON -> stringResource(id = R.string.no)
    }
}
