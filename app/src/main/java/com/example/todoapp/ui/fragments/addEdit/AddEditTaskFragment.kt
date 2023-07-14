package com.example.todoapp.ui.fragments.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.todoapp.appComponent
import com.example.todoapp.ui.fragments.addEdit.compose.AddEditTaskContent
import com.example.todoapp.ui.fragments.addEdit.viewmodel.AddEditTaskViewModel
import com.example.todoapp.ui.theme.TodoAppTheme


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

    @Composable
    fun AddEditTaskScreen(
        viewModel: AddEditTaskViewModel = viewModel()
    ) {
        val viewState by viewModel.uiState.collectAsState()
        val onCloseButtonClicked: () -> Unit = {
            findNavController().popBackStack()
        }
        val onSaveButtonClicked: () -> Unit = {
            viewModel.saveTodoItem(todoItemId)
            findNavController().popBackStack()
        }
        val onDeleteButtonClicked: () -> Unit = {
            todoItemId?.let { viewModel.deleteTodoItem(it) }
            findNavController().popBackStack()
        }

        AddEditTaskContent(
            onCloseButtonClicked = onCloseButtonClicked,
            onSaveButtonClicked = onSaveButtonClicked,
            descriptionText = viewState.text,
            onDescriptionTextChanged = viewModel::setText,
            importance = viewState.importance,
            onImportanceSectionClicked = viewModel::openBottomSheet,
            isDatePickerDialogVisible = viewState.isDatePickerDialogVisible,
            closeDatePicker = viewModel::closeDatePicker,
            deadline = viewState.deadline,
            isDeleteButtonEnabled = viewState.isDeleteButtonEnabled,
            saveDate = viewModel::setDate,
            openDatePicker = viewModel::openDatePicker,
            onDeleteButtonClicked = onDeleteButtonClicked,
            isBottomSheetVisible = viewState.isBottomSheetVisible,
            closeBottomSheet = viewModel::closeBottomSheet,
            setImportance = viewModel::setImportance
        )
    }
}
