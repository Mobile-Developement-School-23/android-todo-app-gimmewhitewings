package com.example.todoapp.ui.fragments.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
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
    fun AddEditTaskScreen(
        viewModel: AddEditTaskViewModel = viewModel()
    ) {
        val viewState by viewModel.uiState.collectAsState()
        val descriptionText = viewState.text
        val importance = viewState.importance
        val deadline = viewState.deadline
        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val onCloseButtonClicked: () -> Unit = {
            findNavController().popBackStack()
        }
        val onSaveButtonClicked: () -> Unit = {
            viewModel.saveTodoItem(todoItemId)
            findNavController().popBackStack()
        }
        val onDescriptionTextChanged = viewModel::setText
        val onImportanceSectionClicked = { showBottomSheet = true }
        var openDatePickerDialog by remember { mutableStateOf(false) }
        val onDateSaved: (Long) -> Unit = {
            viewModel.setDate(Date(it))
            openDatePickerDialog = false
        }
        val onDatePickerDialogDismissRequest = { openDatePickerDialog = false }
        val onDeadlineSectionClicked = { openDatePickerDialog = true }
        val onSwitchCheckedChange: (Boolean) -> Unit = {
            if (it) viewModel.setDate(Date())
            if (!it) viewModel.setDate(null)
        }
        val onDeleteButtonClicked: () -> Unit = {
            todoItemId?.let { viewModel.deleteTodoItem(it) }
            findNavController().popBackStack()
        }
        val radioOptions = listOf(
            Importance.HIGH,
            Importance.COMMON,
            Importance.LOW
        )
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(importance)
        }
        val onSaveImportanceButtonClicked = {
            coroutineScope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    showBottomSheet = false
                }
            viewModel.setImportance(selectedOption)
        }
        val onModalBottomSheetDismissRequest = { showBottomSheet = false }
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
                        descriptionText = descriptionText,
                        onDescriptionTextChanged = onDescriptionTextChanged
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ImportanceSection(
                        importance = importance,
                        onImportanceSectionClicked = onImportanceSectionClicked
                    )

                    Divider()


                    if (openDatePickerDialog) {
                        val datePickerState = rememberDatePickerState()
                        DatePickerDialog(
                            onDismissRequest = onDatePickerDialogDismissRequest,
                            colors = DatePickerDefaults.colors(
                                containerColor = TodoAppTheme.colors.backSecondary
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
                            }
                        ) {
                            DatePicker(
                                state = datePickerState,
                                // TODO: fix this magic number (it's required to allow to set the current day)
                                dateValidator = { it >= System.currentTimeMillis() - 24 * 60 * 60 * 1000 },
                                colors = DatePickerDefaults.colors(
                                    selectedDayContentColor = TodoAppTheme.colors.labelPrimary,
                                    selectedDayContainerColor = TodoAppTheme.colors.colorBlue,
                                    todayContentColor = TodoAppTheme.colors.colorBlue,
                                    todayDateBorderColor = TodoAppTheme.colors.colorBlue
                                )
                            )
                        }
                    }
                    DeadlineSection(
                        deadlineDate = deadline,
                        onDeadlineSectionClicked = onDeadlineSectionClicked,
                        onSwitchCheckedChange = onSwitchCheckedChange
                    )

                    Spacer(Modifier.height(24.dp))
                    Divider()
                    DeleteButton(todoItemId, onDeleteButtonClicked)
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = onModalBottomSheetDismissRequest,
                            containerColor = TodoAppTheme.colors.backSecondary,
                            sheetState = sheetState
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
                                ImportanceRadioButtonGroup(
                                    radioOptions = radioOptions,
                                    selectedOption = selectedOption,
                                    onOptionSelected = onOptionSelected
                                )
                                SaveImportanceButton(onSaveImportanceButtonClicked)
                            }
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun SaveImportanceButton(onSaveImportanceButtonClicked: () -> Unit) {
        TextButton(
            onClick = onSaveImportanceButtonClicked,
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditTaskTopAppBar(
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
fun DescriptionCard(
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

@Composable
fun ImportanceSection(
    importance: Importance,
    onImportanceSectionClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onImportanceSectionClicked() }
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

@Composable
fun ImportanceRadioButtonGroup(
    radioOptions: List<Importance>,
    selectedOption: Importance,
    onOptionSelected: (Importance) -> Unit
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = 0f,
        animationSpec = keyframes {
            durationMillis = 400
            0.2f at 200
            0f at 400
        }
    )
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { importance ->
            val isSelected = importance == selectedOption
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onOptionSelected(importance)
                        },
                        role = Role.RadioButton
                    )
                    .background(
                        color = if (isSelected && importance == Importance.HIGH) {
                            TodoAppTheme.colors.colorRed.copy(
                                alpha = animatedAlpha
                            )
                        } else Color.Transparent
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = isSelected,
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
fun DeadlineSection(
    deadlineDate: Date?,
    onSwitchCheckedChange: (Boolean) -> Unit,
    onDeadlineSectionClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = deadlineDate != null,
                onClick = onDeadlineSectionClicked,
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

@Composable
fun DeleteButton(
    todoItemId: String?,
    onDeleteButtonClicked: () -> Unit
) {
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
}

@Composable
fun Importance.toStringResource(): String = when (this) {
    Importance.HIGH -> stringResource(id = R.string.high)
    Importance.LOW -> stringResource(id = R.string.low)
    Importance.COMMON -> stringResource(id = R.string.no)
}
