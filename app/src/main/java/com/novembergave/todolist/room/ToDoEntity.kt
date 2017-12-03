package com.novembergave.todolist.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
import android.provider.BaseColumns


//const val TABLE_NAME: String = "to_do_list"
//@Entity(tableName = TABLE_NAME)
//data class ToDoEntity constructor(@ColumnInfo(name = "id")
//                                  @PrimaryKey(autoGenerate = true) var id: Long,
//                                  @ColumnInfo(name = "title") var title: String,
//                                  @ColumnInfo(name = "date_added") val dateAdded: Long,
//                                  @ColumnInfo(name = "date_completed") val dateCompleted: Long?,
//                                  @ColumnInfo(name = "priority") val priority: String) {
//}
const val TABLE_NAME = "to_do_list"
const val COLUMN_ID = BaseColumns._ID
const val COLUMN_NAME = "name"
const val COLUMN_ADDED = "date_added"
const val COLUMN_DONE = "date_completed"
const val COLUMN_PRIORITY = "priority"

@Entity(tableName = TABLE_NAME)
class ToDoEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    var id: Long = 0

    @ColumnInfo(name = COLUMN_NAME)
    var name: String = ""

    @ColumnInfo(name = COLUMN_ADDED)
    var dateAdded: Long = 0

    @ColumnInfo(name = COLUMN_DONE)
    var dateCompleted: Long = 0

    @ColumnInfo(name = COLUMN_PRIORITY)
    var priority: String = ""


    companion object {

        fun fromContentValues(values: ContentValues?): ToDoEntity {
            val item = ToDoEntity()
            if (values!!.containsKey(COLUMN_ID)) {
                item.id = values.getAsLong(COLUMN_ID)!!
            }
            if (values.containsKey(COLUMN_NAME)) {
                item.name = values.getAsString(COLUMN_NAME)
            }
            if (values.containsKey(COLUMN_ADDED)) {
                item.dateAdded = values.getAsLong(COLUMN_ADDED)
            }
            if (values.containsKey(COLUMN_DONE)) {
                item.dateCompleted = values.getAsLong(COLUMN_DONE)
            }
            if (values.containsKey(COLUMN_PRIORITY)) {
                item.priority = values.getAsString(COLUMN_PRIORITY)
            }
            return item
        }
    }

}
