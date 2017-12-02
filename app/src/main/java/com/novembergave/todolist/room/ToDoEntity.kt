package com.novembergave.todolist.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "to_do_list")
data class ToDoEntity constructor(@ColumnInfo(name = "title") val title: String,
                                  @ColumnInfo(name = "date_added") val dateAdded: Long,
                                  @ColumnInfo(name = "date_completed") val dateCompleted: Long?,
                                  @ColumnInfo(name = "priority") val priority: String) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}