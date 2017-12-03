package com.novembergave.todolist.room

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createItem(user: ToDoEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    fun findAll(): Flowable<List<ToDoEntity>>

    @Query("SELECT * FROM $TABLE_NAME where date_completed > 0")
    fun findAllCompleted(): Flowable<List<ToDoEntity>>

    @Query("SELECT * FROM $TABLE_NAME where date_completed = 0")
    fun findAllOutstanding(): Flowable<List<ToDoEntity>>

    @Query("select * from $TABLE_NAME where id = :id")
    fun findItemById(id: Long): Flowable<ToDoEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: ToDoEntity)

    @Delete
    fun deleteItem(item: ToDoEntity)
}