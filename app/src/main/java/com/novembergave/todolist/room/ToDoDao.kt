package com.novembergave.todolist.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor

@Dao
interface ToDoDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun createItem(user: ToDoEntity)
//
//    @Query("SELECT * FROM to_do_list")
//    fun findAll(): Flowable<List<ToDoEntity>>
//
//    @Query("SELECT * FROM to_do_list where date_completed > 0")
//    fun findAllCompleted(): Flowable<List<ToDoEntity>>
//
//    @Query("SELECT * FROM to_do_list where date_completed = 0")
//    fun findAllOutstanding(): Flowable<List<ToDoEntity>>
//
//    @Query("select * from to_do_list where id = :id")
//    fun findItemById(id: Long): Flowable<ToDoEntity>
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    fun updateItem(item: ToDoEntity)
//
//    @Delete
//    fun deleteItem(item: ToDoEntity)


    @Query("SELECT COUNT(*) FROM " + TABLE_NAME)
    fun count(): Int

    @Insert
    fun insert(cheese: ToDoEntity): Long

    @Insert
    fun insertAll(cheeses: Array<ToDoEntity?>): LongArray

    @Query("SELECT * FROM " + TABLE_NAME)
    fun selectAll(): Cursor

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    fun selectById(id: Long): Cursor

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    fun deleteById(id: Long): Int

    @Update
    fun update(cheese: ToDoEntity): Int
}