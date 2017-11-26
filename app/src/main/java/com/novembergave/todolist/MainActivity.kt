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
        dummyList.add(ToDoItem("One", "25th November 2017", ToDoItem.Priority.HIGH))
        dummyList.add(ToDoItem("Two", "24th November 2017", ToDoItem.Priority.MEDIUM))
        dummyList.add(ToDoItem("Three", "23th November 2017", ToDoItem.Priority.LOW))
    }

}
