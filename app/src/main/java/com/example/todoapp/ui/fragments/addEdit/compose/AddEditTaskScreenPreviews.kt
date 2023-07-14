package com.example.todoapp.ui.fragments.addEdit.compose

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.data.model.Importance
import com.example.todoapp.ui.fragments.addEdit.viewmodel.AddEditUiState
import com.example.todoapp.ui.theme.TodoAppTheme
import java.util.Date


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewAddEditTaskContent_newTask() {
    TodoAppTheme {
        val uiState = AddEditUiState()
        AddEditTaskContent(
            descriptionText = uiState.text,
            isDeleteButtonEnabled = uiState.isDeleteButtonEnabled,
            importance = uiState.importance,
            setImportance = {},
            closeDatePicker = { },
            openDatePicker = { },
            isDatePickerDialogVisible = uiState.isDatePickerDialogVisible,
            deadline = uiState.deadline,
            saveDate = {},
            isBottomSheetVisible = uiState.isBottomSheetVisible,
            closeBottomSheet = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewAddEditTaskContent_editTask() {
    TodoAppTheme {
        val uiState = AddEditUiState(
            text = "Edit task",
            importance = Importance.HIGH,
            deadline = Date(),
            isDeleteButtonEnabled = true,
        )
        AddEditTaskContent(
            descriptionText = uiState.text,
            isDeleteButtonEnabled = uiState.isDeleteButtonEnabled,
            importance = uiState.importance,
            setImportance = {},
            closeDatePicker = { },
            openDatePicker = { },
            isDatePickerDialogVisible = uiState.isDatePickerDialogVisible,
            deadline = uiState.deadline,
            saveDate = {},
            isBottomSheetVisible = uiState.isBottomSheetVisible,
            closeBottomSheet = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDatePickerDialog() {
    TodoAppTheme {
        DeadlineDatePickerDialog(
            closeDatePicker = { },
            onDatePickerConfirmButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewBottomSheet() {
    TodoAppTheme {
        ImportanceBottomSheet(
            setImportance = {},
            closeBottomSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewTopAppBar() {
    TodoAppTheme {
        AddEditTaskTopAppBar(
            onCloseButtonClicked = {},
            onSaveButtonClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDescriptionCard() {
    TodoAppTheme {
        DescriptionCard(descriptionText = "Lorem ipsum") {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDescriptionCard_noDescription() {
    TodoAppTheme {
        DescriptionCard(descriptionText = "") {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewImportanceSection() {
    TodoAppTheme {
        ImportanceSection(importance = Importance.COMMON) {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeadlineSection_noDeadline() {
    TodoAppTheme {
        DeadlineSection(
            deadlineDate = null,
            onSwitchCheckedChange = {}
        ) { }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeadlineSection() {
    TodoAppTheme {
        DeadlineSection(
            deadlineDate = Date(),
            onSwitchCheckedChange = {}
        ) {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeleteButton() {
    TodoAppTheme {
        DeleteButton(true) {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeleteButton_newTodoItem() {
    TodoAppTheme {
        DeleteButton(false) {}
    }
}

