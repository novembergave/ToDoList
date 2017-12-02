package com.novembergave.todolist.utils

import com.novembergave.todolist.ToDoItem


class CompareOutstandingToDos {
    companion object : Comparator<ToDoItem> {

        override fun compare(a: ToDoItem, b: ToDoItem): Int = when {
            a.priority != b.priority -> a.priority.ordinal - b.priority.ordinal
            a.dateAdded != b.dateAdded -> (a.dateAdded - b.dateAdded).toInt()
            else -> a.title.compareTo(b.title)
        }
    }
}