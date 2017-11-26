package com.novembergave.todolist

import java.io.Serializable


class ToDoItem(title: String, date: String, priority: Priority) : Serializable {
    var title: String = title
    var date: String = date
    var priority: Priority = priority

    enum class Priority {
        HIGH, MEDIUM, LOW
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToDoItem

        if (title != other.title) return false
        if (date != other.date) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + priority.hashCode()
        return result
    }

    override fun toString(): String {
        return "ToDoItem(title='$title', date='$date', priority=$priority)"
    }

}