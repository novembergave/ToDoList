package com.novembergave.todolist.room

import android.arch.persistence.room.*

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createItem(user: ToDoEntity)

    @Query("SELECT * FROM to_do_list")
    fun findAll(): List<ToDoEntity>

    @Update
    fun updateItem(user: ToDoEntity)

    @Delete
    fun deleteItem(user: ToDoEntity)
}