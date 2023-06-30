package com.example.todoapp.data.source

import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class TodoItemsFakeDataSource : TodoItemsDataSource {
    private val todoItemsList = listOf(
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

    val todoItems = MutableStateFlow(todoItemsList)

    override suspend fun getAllTodoItems(): Flow<List<TodoItem>> = todoItems

    override suspend fun addTodoItem(newTodoItem: TodoItem) {
        val currentList = todoItems.value
        val updatedList = currentList.toMutableList()
        updatedList.add(0, newTodoItem)
        todoItems.value = updatedList
    }

    suspend fun updateOrAddTodoItem(todoItem: TodoItem) {
        val currentList = todoItems.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOfFirst {
            it.id == todoItem.id
        }
        if (index == -1) {
            updatedList.add(0, todoItem)
        } else {
            updatedList[index] = todoItem
        }
        todoItems.value = updatedList
    }

    override suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItems.value.find { it.id == itemId }
    }

    override suspend fun deleteTodoItemById(itemId: String) {
        val currentList = todoItems.value
        val updatedList = currentList.toMutableList()
        updatedList.removeIf { it.id == itemId }
        todoItems.value = updatedList
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        val currentList = todoItems.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOfFirst { it.id == todoItem.id }
        updatedList[index] = todoItem
        todoItems.value = updatedList.toMutableList()
    }

    override suspend fun toggleTodoItemCompletionById(todoItemId: String) {
        val currentList = todoItems.value
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOfFirst {
            it.id == todoItemId
        }
        updatedList[index] = updatedList[index].copy(isCompleted = !updatedList[index].isCompleted)
        todoItems.value = updatedList.toMutableList()
    }

    override suspend fun removeTodoItem(todoItem: TodoItem) {
        val currentList = todoItems.value
        if (currentList.isNotEmpty()) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(todoItem)
            todoItems.value = updatedList
        }
    }
}