package com.novembergave.todolist.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val TABLE_NAME = "to_do_list"

@Entity(tableName = TABLE_NAME)
data class ToDoEntity constructor(@ColumnInfo(name = "id")
                                  @PrimaryKey(autoGenerate = true) val id: Long,
                                  @ColumnInfo(name = "title") val title: String,
                                  @ColumnInfo(name = "date_added") val dateAdded: Long,
                                  @ColumnInfo(name = "date_completed") val dateCompleted: Long?,
                                  @ColumnInfo(name = "priority") val priority: String) {
}