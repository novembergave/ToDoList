package com.novembergave.todolist

import java.io.Serializable


class ToDoItem(title: String, dateAdded: Long, dateCompleted: Long?, priority: Priority) : Serializable {
    var title: String = title
    var dateAdded: Long = dateAdded
    var dateCompleted: Long? = dateCompleted
    var priority: Priority = priority

    enum class Priority {
        HIGH, MEDIUM, LOW
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToDoItem

        if (title != other.title) return false
        if (dateAdded != other.dateAdded) return false
        if (dateCompleted != other.dateCompleted) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + dateAdded.hashCode()
        result = 31 * result + (dateCompleted?.hashCode() ?: 0)
        result = 31 * result + priority.hashCode()
        return result
    }

    override fun toString(): String {
        return "ToDoItem(title='$title', dateAdded='$dateAdded', dateCompleted=$dateCompleted, priority=$priority)"
    }


}