package com.novembergave.todolist.dagger

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.novembergave.todolist.room.ToDoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application


    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): ToDoDatabase =
            Room.databaseBuilder(context, ToDoDatabase::class.java, "to_do_list.db").build()

    @Provides
    fun providesToDoDao(toDoDatabase: ToDoDatabase) = toDoDatabase.toDoDao()
}
