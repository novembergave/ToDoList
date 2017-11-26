package com.novembergave.todolist

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AddDialog.SaveNewToDoItem {
    override fun addItem(item: ToDoItem) {
        dummyList.add(item)
        adapter.updateList(dummyList)
    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter
    private var dummyList: MutableList<ToDoItem> = ArrayList()
    private lateinit var fabButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabButton = findViewById(R.id.fab_button)
        fabButton.setOnClickListener {
            val pop = AddDialog()
            val fm = this@MainActivity.fragmentManager
            pop.show(fm, "name")
        }

        initialiseDummyList()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter(dummyList)
        recyclerview.adapter = adapter

    }

    private fun initialiseDummyList() {
        dummyList.add(ToDoItem("One", "Sun, 26 Nov 2017, 20:58", ToDoItem.Priority.HIGH))
        dummyList.add(ToDoItem("Two", "Sat, 25 Nov 2017, 10:50", ToDoItem.Priority.MEDIUM))
        dummyList.add(ToDoItem("Three", "Fri, 24 Nov 2017, 12:00", ToDoItem.Priority.LOW))
    }

}
