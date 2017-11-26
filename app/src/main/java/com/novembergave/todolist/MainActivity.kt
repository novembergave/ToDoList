package com.novembergave.todolist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter
    var dummyList: List<ToDoItem> = listOf(
            ToDoItem("One", "25th November 2017", ToDoItem.Priority.HIGH),
            ToDoItem("Two", "24th November 2017", ToDoItem.Priority.MEDIUM),
            ToDoItem("Three", "23th November 2017", ToDoItem.Priority.LOW))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = linearLayoutManager

        adapter = RecyclerViewAdapter(dummyList)
        recyclerview.adapter = adapter
    }
}
