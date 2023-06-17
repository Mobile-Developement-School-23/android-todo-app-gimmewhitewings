package com.example.todoapp.data.source

import com.example.todoapp.data.models.Importance
import com.example.todoapp.data.models.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class TodoItemsHardcodeDataSource : TodoItemsDataSource {
    private val todoItems = listOf(
        TodoItem(
            "1",
            "Купить кое-что",
            Importance.COMMON,
            false, Date()
        ),
        TodoItem(
            "2",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            Importance.COMMON,
            false,
            Date()
        ),
        TodoItem(
            "3",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается очень длинный текст, а дальше просто пойдет набор букв аафдлвоа афлоа фдоваф дла адфлоаоаа йоашйоцоа оо",
            Importance.COMMON,
            false,
            Date()
        ),
        TodoItem(
            "4",
            "Купить что-то",
            Importance.LOW,
            false,
            Date()
        ),
        TodoItem(
            "5",
            "Купить что-то",
            Importance.HIGH,
            false,
            Date()
        ),
        TodoItem(
            "6",
            "Купить что-то",
            Importance.COMMON,
            true,
            Date()
        ),
        TodoItem(
            "7",
            "Закончить проект",
            Importance.HIGH,
            false,
            Date(),
            Date(),
            Date()
        ),
        TodoItem(
            "8",
            "Поехать в отпуск",
            Importance.COMMON,
            false,
            Date(),
            Date(),
            null
        ),
        TodoItem(
            "9",
            "Посмотреть новый фильм",
            Importance.LOW,
            false,
            Date(),
            null,
            null
        ),
        TodoItem(
            "10",
            "Подготовиться к собеседованию",
            Importance.HIGH,
            false,
            Date(),
            Date(),
            null
        ),
        TodoItem(
            "11",
            "Починить сломанную мебель",
            Importance.COMMON,
            true,
            Date(),
            null,
            Date()
        ),
        TodoItem(
            "12",
            "Прочитать новую книгу",
            Importance.LOW,
            false,
            Date(),
            null,
            null
        ),
        TodoItem(
            "13",
            "Организовать день рождения",
            Importance.HIGH,
            false,
            Date(),
            Date(),
            null
        ),
        TodoItem(
            "14",
            "Пойти на тренировку",
            Importance.COMMON,
            false,
            Date(),
            null,
            null
        ),
        TodoItem(
            "15",
            "Подготовить ужин",
            Importance.LOW,
            true,
            Date(),
            null,
            Date()
        ),
        TodoItem(
            "16",
            "Посетить музей",
            Importance.HIGH,
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

    override suspend fun updateOrAddTodoItem(todoItem: TodoItem) {
        val currentList = todoItemsFlow.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOfFirst {
            it.id == todoItem.id
        }
        if (index == -1) {
            updatedList.add(0, todoItem)
        } else {
            updatedList[index] = todoItem
        }
        todoItemsFlow.value = updatedList
    }

    override suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItemsFlow.value.find { it.id == itemId }
    }

    override suspend fun deleteTodoItemById(itemId: String) {
        val currentList = todoItemsFlow.value
        val updatedList = currentList.toMutableList()
        updatedList.removeIf { it.id == itemId }
        todoItemsFlow.value = updatedList
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        val currentList = todoItemsFlow.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOf(todoItem)
        updatedList[index] = todoItem
        todoItemsFlow.value = updatedList.toMutableList()
    }

    override suspend fun toggleStatus(todoItem: TodoItem) {
        val currentList = todoItemsFlow.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOfFirst {
            it.id == todoItem.id
        }
        updatedList[index] = updatedList[index].copy(isCompleted = !todoItem.isCompleted)
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