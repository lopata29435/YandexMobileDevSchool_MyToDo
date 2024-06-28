package com.example.mytodolist

class TodoItemsRepository {
    private val todoList = mutableListOf<TodoItem>()

    fun getAllTodoItems(): List<TodoItem> {
        return todoList.toList()
    }

    fun addTodoItem(todoItem: TodoItem) {
        todoList.add(todoItem)
    }

    fun size(): Int{
        return todoList.size;
    }

    init {
        todoList.addAll(listOf(
            TodoItem(1, "Закончить проект 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 1, false, System.currentTimeMillis() + 1000 * 1, System.currentTimeMillis() + 1000 * 1),
            TodoItem(2, "Сделать покупки 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 2, false, System.currentTimeMillis() + 1000 * 2, System.currentTimeMillis() + 1000 * 2),
            TodoItem(3, "Позвонить маме 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 3, false, System.currentTimeMillis() + 1000 * 3, System.currentTimeMillis() + 1000 * 3),
            TodoItem(4, "Погулять с собакой 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 4, false, System.currentTimeMillis() + 1000 * 4, System.currentTimeMillis() + 1000 * 4),
            TodoItem(5, "Почистить зубы 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 5, false, System.currentTimeMillis() + 1000 * 5, System.currentTimeMillis() + 1000 * 5),
            TodoItem(6, "Посмотреть фильм 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 6, false, System.currentTimeMillis() + 1000 * 6, System.currentTimeMillis() + 1000 * 6),
            TodoItem(7, "Сделать зарядку 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 7, false, System.currentTimeMillis() + 1000 * 7, System.currentTimeMillis() + 1000 * 7),
            TodoItem(8, "Почитать книгу 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 8, false, System.currentTimeMillis() + 1000 * 8, System.currentTimeMillis() + 1000 * 8),
            TodoItem(9, "Приготовить ужин 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 9, false, System.currentTimeMillis() + 1000 * 9, System.currentTimeMillis() + 1000 * 9),
            TodoItem(10, "Выгулять ребенка 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 10, false, System.currentTimeMillis() + 1000 * 10, System.currentTimeMillis() + 1000 * 10),
            TodoItem(11, "Сделать фото 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 11, false, System.currentTimeMillis() + 1000 * 11, System.currentTimeMillis() + 1000 * 11),
            TodoItem(12, "Сделать пазл 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 12, false, System.currentTimeMillis() + 1000 * 12, System.currentTimeMillis() + 1000 * 12),
            TodoItem(13, "Забрать посылку 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 13, false, System.currentTimeMillis() + 1000 * 13, System.currentTimeMillis() + 1000 * 13),
            TodoItem(14, "Отправить письмо 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 14, false, System.currentTimeMillis() + 1000 * 14, System.currentTimeMillis() + 1000 * 14),
            TodoItem(15, "Сходить на выставку 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 15, false, System.currentTimeMillis() + 1000 * 15, System.currentTimeMillis() + 1000 * 15),
            TodoItem(16, "Купить подарок 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 16, false, System.currentTimeMillis() + 1000 * 16, System.currentTimeMillis() + 1000 * 16),
            TodoItem(17, "Сходить в бассейн 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 17, false, System.currentTimeMillis() + 1000 * 17, System.currentTimeMillis() + 1000 * 17),
            TodoItem(18, "Сходить в парк 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 18, false, System.currentTimeMillis() + 1000 * 18, System.currentTimeMillis() + 1000 * 18),
            TodoItem(19, "Съездить на дачу 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 19, false, System.currentTimeMillis() + 1000 * 19, System.currentTimeMillis() + 1000 * 19),
            TodoItem(20, "Починить кран 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 20, false, System.currentTimeMillis() + 1000 * 20, System.currentTimeMillis() + 1000 * 20),
            TodoItem(21, "Посадить цветы 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 21, false, System.currentTimeMillis() + 1000 * 21, System.currentTimeMillis() + 1000 * 21),
            TodoItem(22, "Сходить в кино 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 22, false, System.currentTimeMillis() + 1000 * 22, System.currentTimeMillis() + 1000 * 22),
            TodoItem(23, "Поехать на море 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 23, false, System.currentTimeMillis() + 1000 * 23, System.currentTimeMillis() + 1000 * 23),
            TodoItem(24, "Починить велосипед 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 24, false, System.currentTimeMillis() + 1000 * 24, System.currentTimeMillis() + 1000 * 24),
            TodoItem(25, "Покрасить забор 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 25, false, System.currentTimeMillis() + 1000 * 25, System.currentTimeMillis() + 1000 * 25),
            TodoItem(26, "Приготовить завтрак 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 26, false, System.currentTimeMillis() + 1000 * 26, System.currentTimeMillis() + 1000 * 26),
            TodoItem(27, "Сходить на концерт 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 27, false, System.currentTimeMillis() + 1000 * 27, System.currentTimeMillis() + 1000 * 27),
            TodoItem(28, "Сделать уборку 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 28, false, System.currentTimeMillis() + 1000 * 28, System.currentTimeMillis() + 1000 * 28),
            TodoItem(29, "Сделать фото 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 29, false, System.currentTimeMillis() + 1000 * 29, System.currentTimeMillis() + 1000 * 29),
            TodoItem(30, "Сделать пазл 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 30, false, System.currentTimeMillis() + 1000 * 30, System.currentTimeMillis() + 1000 * 30),
            TodoItem(31, "Забрать посылку 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 31, false, System.currentTimeMillis() + 1000 * 31, System.currentTimeMillis() + 1000 * 31),
            TodoItem(32, "Отправить письмо 1", Priority.NORMAL, System.currentTimeMillis() + 86400000 * 32, false, System.currentTimeMillis() + 1000 * 32, System.currentTimeMillis() + 1000 * 32),
            TodoItem(33, "Сходить на выставку 1", Priority.LOW, System.currentTimeMillis() + 86400000 * 33, false, System.currentTimeMillis() + 1000 * 33, System.currentTimeMillis() + 1000 * 33),
            TodoItem(34, "Купить подарок 1", Priority.HIGH, System.currentTimeMillis() + 86400000 * 34, false, System.currentTimeMillis() + 1000 * 34, System.currentTimeMillis() + 1000 * 34),
        ))
    }
}