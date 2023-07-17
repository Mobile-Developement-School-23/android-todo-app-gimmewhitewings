package com.example.todoapp.ui.fragments.addEdit.compose

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.utils.DateFormatter
import kotlinx.coroutines.launch
import java.util.Date


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditTaskTopAppBar(
    onCloseButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TodoAppTheme.colors.backPrimary
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskContent(
    onCloseButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    descriptionText: String,
    onDescriptionTextChanged: (String) -> Unit = {},
    isDeleteButtonEnabled: Boolean,
    importance: Importance,
    setImportance: (Importance) -> Unit,
    closeDatePicker: () -> Unit,
    openDatePicker: () -> Unit,
    onImportanceSectionClicked: () -> Unit = {},
    isDatePickerDialogVisible: Boolean,
    deadline: Date?,
    saveDate: (Date?) -> Unit,
    onDeleteButtonClicked: () -> Unit = {},
    isBottomSheetVisible: Boolean,
    closeBottomSheet: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = TodoAppTheme.colors.backPrimary,
        topBar = {
            AddEditTaskTopAppBar(
                onCloseButtonClicked = onCloseButtonClicked,
                onSaveButtonClicked = onSaveButtonClicked,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
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
            if (isBottomSheetVisible) {
                ImportanceBottomSheet(setImportance, closeBottomSheet)
            }

            Divider()

            val onSwitchCheckedChange: (Boolean) -> Unit = {
                if (it) {
                    saveDate(Date())
                } else {
                    saveDate(null)
                }
            }
            DeadlineSection(
                deadlineDate = deadline,
                onDeadlineSectionClicked = openDatePicker,
                onSwitchCheckedChange = onSwitchCheckedChange
            )
            if (isDatePickerDialogVisible) {
                val onDatePickerConfirmButtonClick: (Long) -> Unit = {
                    saveDate(Date(it))
                    closeDatePicker()
                }
                DeadlineDatePickerDialog(closeDatePicker, onDatePickerConfirmButtonClick)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()

            DeleteButton(
                isEnabled = isDeleteButtonEnabled,
                onDeleteButtonClicked = onDeleteButtonClicked
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ImportanceBottomSheet(
    setImportance: (Importance) -> Unit,
    closeBottomSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val rememberCoroutineScope = rememberCoroutineScope()
    val onButtonClick: (Importance) -> Unit = {
        setImportance(it)
        rememberCoroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
            closeBottomSheet()
        }
    }
    ModalBottomSheet(
        onDismissRequest = closeBottomSheet,
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
            listOf(
                Importance.HIGH,
                Importance.COMMON,
                Importance.LOW
            ).forEach { importance ->
                ImportanceButton(
                    importance = importance,
                    onButtonClick = onButtonClick
                )
            }
        }
    }
}

@Composable
fun ImportanceButton(importance: Importance, onButtonClick: (Importance) -> Unit) {
    val color = when (importance) {
        Importance.LOW -> TodoAppTheme.colors.colorBlue
        Importance.COMMON -> TodoAppTheme.colors.colorGreen
        Importance.HIGH -> TodoAppTheme.colors.colorRed
    }
    TextButton(
        onClick = { onButtonClick(importance) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = color
        )
    ) {
        Text(
            text = importance.toStringResource(),
            style = TodoAppTheme.typography.body,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeadlineDatePickerDialog(
    closeDatePicker: () -> Unit,
    onDatePickerConfirmButtonClick: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = closeDatePicker,
        colors = DatePickerDefaults.colors(
            containerColor = TodoAppTheme.colors.backSecondary
        ),
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = { onDatePickerConfirmButtonClick(datePickerState.selectedDateMillis!!) },
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
    isEnabled: Boolean,
    onDeleteButtonClicked: () -> Unit
) {
    TextButton(
        onClick = onDeleteButtonClicked,
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TodoAppTheme.colors.colorRed,
            disabledContentColor = TodoAppTheme.colors.labelDisable,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(
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
