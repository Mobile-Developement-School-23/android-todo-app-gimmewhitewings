package com.example.todoapp.data.source

import com.example.todoapp.data.models.Importance
import com.example.todoapp.data.models.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class TodoItemsHardcodeDataSource : TodoItemsDataSource {
    private val todoItems: MutableList<TodoItem> = mutableListOf(
        TodoItem(
            "1",
            "Купить кое-что",
            Importance.MIDDLE,
            false, Date()
        ),
        TodoItem(
            "2",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            Importance.MIDDLE,
            false,
            Date()
        ),
        TodoItem(
            "3",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается очень длинный текст, а дальше просто пойдет набор букв аафдлвоа афлоа фдоваф дла адфлоаоаа йоашйоцоа оо",
            Importance.MIDDLE,
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
            Importance.MIDDLE,
            true,
            Date()
        ),
        TodoItem(
            "7",
            "Закончить проект",
            Importance.HIGH,
            false,
            Date(),
            Date(2023, 6, 30),
            Date()
        ),
        TodoItem(
            "8",
            "Поехать в отпуск",
            Importance.MIDDLE,
            false,
            Date(),
            Date(2023, 7, 15),
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
            Date(2023, 6, 20),
            null
        ),
        TodoItem(
            "11",
            "Починить сломанную мебель",
            Importance.MIDDLE,
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
            Date(2023, 8, 10),
            null
        ),
        TodoItem(
            "14",
            "Пойти на тренировку",
            Importance.MIDDLE,
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
            Date(2023, 7, 1),
            null
        )
    )

    override fun getAllTodoItems(): Flow<List<TodoItem>> {
        return flow { emit(todoItems) }
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        todoItems.add(todoItem)
    }
}