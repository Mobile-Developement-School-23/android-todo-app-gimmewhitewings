package com.example.todoapp.ui.fragments.addEdit

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.data.model.Importance
import com.example.todoapp.ui.theme.TodoAppTheme
import java.util.Date


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewTopAppBar() {
    TodoAppTheme {
        AddEditTaskTopAppBar({}, {})
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
        ImportanceSection(Importance.COMMON) {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeadlineSection_noDeadline() {
    TodoAppTheme {
        DeadlineSection(
            deadlineDate = null,
            onSwitchCheckedChange = {}) { }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeadlineSection() {
    TodoAppTheme {
        DeadlineSection(
            deadlineDate = Date(),
            onSwitchCheckedChange = {}) {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeleteButton() {
    TodoAppTheme {
        DeleteButton("1") {}
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "night")
@Composable
fun PreviewDeleteButton_newTodoItem() {
    TodoAppTheme {
        DeleteButton(null) {}
    }
}

