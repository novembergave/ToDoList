package com.novembergave.todolist


class ToDoItem(title: String, date: String, priority: Priority) {
    var title: String = title
    var date: String = date
    var priority: Priority = priority

    enum class Priority {
        HIGH, MEDIUM, LOW
    }
}