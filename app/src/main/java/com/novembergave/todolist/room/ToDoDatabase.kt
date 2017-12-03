package com.novembergave.todolist.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.VisibleForTesting

//@Database(entities = arrayOf(ToDoEntity::class), version = 1, exportSchema = false)
//abstract class ToDoDatabase : RoomDatabase() {
//
//    abstract fun toDoDao(): ToDoDao
//}

@Database(entities = arrayOf(ToDoEntity::class), version = 1)
abstract class ToDoDatabase : RoomDatabase() {

    /**
     * @return The DAO for the Cheese table.
     */
    abstract fun cheese(): ToDoDao

    companion object {

        /** The only instance  */
        private var sInstance: ToDoDatabase? = null

        /**
         * Gets the singleton instance of SampleDatabase.
         *
         * @param context The context.
         * @return The singleton instance of SampleDatabase.
         */
        fun getInstance(context: Context): ToDoDatabase {
            if (sInstance == null) {
                sInstance = Room
                        .databaseBuilder<ToDoDatabase>(context.applicationContext, ToDoDatabase::class.java, "ex")
                        .build()
            }
            return sInstance as ToDoDatabase
        }

        /**
         * Switches the internal implementation with an empty in-memory database.
         *
         * @param context The context.
         */
        @VisibleForTesting
        fun switchToInMemory(context: Context) {
            sInstance = Room.inMemoryDatabaseBuilder<ToDoDatabase>(context.applicationContext,
                    ToDoDatabase::class.java).build()
        }
    }

}
