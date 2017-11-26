package com.novembergave.todolist

import java.io.Serializable


class ToDoItem(title: String, date: String, priority: Priority) : Serializable {
    var title: String = title
    var date: String = date
    var priority: Priority = priority

    enum class Priority {
        HIGH, MEDIUM, LOW
    }
}