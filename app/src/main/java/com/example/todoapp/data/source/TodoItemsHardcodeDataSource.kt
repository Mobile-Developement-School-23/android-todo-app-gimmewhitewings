package com.example.todoapp.data.source

import android.util.Log
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class TodoItemsHardcodeDataSource : TodoItemsDataSource {
    private val todoItems = listOf(
        TodoItem(
            "1",
            "Купить кое-что",
            Priority.MIDDLE,
            false, Date()
        ),
        TodoItem(
            "2",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            Priority.MIDDLE,
            false,
            Date()
        ),
        TodoItem(
            "3",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается очень длинный текст, а дальше просто пойдет набор букв аафдлвоа афлоа фдоваф дла адфлоаоаа йоашйоцоа оо",
            Priority.MIDDLE,
            false,
            Date()
        ),
        TodoItem(
            "4",
            "Купить что-то",
            Priority.LOW,
            false,
            Date()
        ),
        TodoItem(
            "5",
            "Купить что-то",
            Priority.HIGH,
            false,
            Date()
        ),
        TodoItem(
            "6",
            "Купить что-то",
            Priority.MIDDLE,
            true,
            Date()
        ),
        TodoItem(
            "7",
            "Закончить проект",
            Priority.HIGH,
            false,
            Date(),
            Date(),
            Date()
        ),
        TodoItem(
            "8",
            "Поехать в отпуск",
            Priority.MIDDLE,
            false,
            Date(),
            Date(),
            null
        ),
        TodoItem(
            "9",
            "Посмотреть новый фильм",
            Priority.LOW,
            false,
            Date(),
            null,
            null
        ),
        TodoItem(
            "10",
            "Подготовиться к собеседованию",
            Priority.HIGH,
            false,
            Date(),
            Date(),
            null
        ),
        TodoItem(
            "11",
            "Починить сломанную мебель",
            Priority.MIDDLE,
            true,
            Date(),
            null,
            Date()
        ),
        TodoItem(
            "12",
            "Прочитать новую книгу",
            Priority.LOW,
            false,
            Date(),
            null,
            null
        ),
        TodoItem(
            "13",
            "Организовать день рождения",
            Priority.HIGH,
            false,
            Date(),
            Date(),
            null
        ),
        TodoItem(
            "14",
            "Пойти на тренировку",
            Priority.MIDDLE,
            false,
            Date(),
            null,
            null
        ),
        TodoItem(
            "15",
            "Подготовить ужин",
            Priority.LOW,
            true,
            Date(),
            null,
            Date()
        ),
        TodoItem(
            "16",
            "Посетить музей",
            Priority.HIGH,
            false,
            Date(),
            Date(),
            null
        )
    )

    private val todoItemsFlow = MutableStateFlow(todoItems)

    override fun getAllTodoItems(): Flow<List<TodoItem>> = todoItemsFlow

    override suspend fun addTodoItem(newTodoItem: TodoItem) {
        val currentList = todoItemsFlow.value
        val updatedList = currentList.toMutableList()
        updatedList.add(0, newTodoItem)
        todoItemsFlow.value = updatedList
    }

    override suspend fun toggleStatus(todoItem: TodoItem) {
        val currentList = todoItemsFlow.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOf(todoItem)
        Log.d("DS", "toggleStatus: ${updatedList[index].isDone}")
        updatedList[index] = updatedList[index].copy(isDone = !todoItem.isDone)
        Log.d("DS", "toggleStatus: ${updatedList[index].isDone}")
        todoItemsFlow.value = updatedList.toMutableList()
    }

    override suspend fun removeTodoItem(todoItem: TodoItem) {
        val currentList = todoItemsFlow.value
        if (currentList.isNotEmpty()) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(todoItem)
            todoItemsFlow.value = updatedList
        }
    }
}