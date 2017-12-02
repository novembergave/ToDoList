package com.novembergave.todolist.dagger

import com.novembergave.todolist.App
import com.novembergave.todolist.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: App)
    fun inject(mainActivity: MainActivity)
}