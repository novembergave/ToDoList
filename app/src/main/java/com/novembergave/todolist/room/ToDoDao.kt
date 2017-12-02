package com.novembergave.todolist.room

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createItem(user: ToDoEntity)

    @Query("SELECT * FROM to_do_list")
    fun findAll(): Flowable<List<ToDoEntity>>

    @Query("select * from to_do_list where id = :id")
    fun findItemById(id: Long): Flowable<ToDoEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: ToDoEntity)

    @Delete
    fun deleteItem(item: ToDoEntity)
}