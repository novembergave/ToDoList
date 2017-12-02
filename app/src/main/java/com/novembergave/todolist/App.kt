package com.novembergave.todolist

import android.app.Application
import com.novembergave.todolist.dagger.AppComponent
import com.novembergave.todolist.dagger.AppModule
import com.novembergave.todolist.dagger.DaggerAppComponent
import com.novembergave.todolist.room.ToDoDatabase
import javax.inject.Inject


class App : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    @Inject
    lateinit var toDoDatabase: ToDoDatabase

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        component.inject(this)
    }
}
