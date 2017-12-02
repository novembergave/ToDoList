package com.novembergave.todolist

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
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
        adapter = RecyclerViewAdapter(dummyList, this::onItemChecked)
        recyclerview.adapter = adapter

    }

    private fun initialiseDummyList() {
        dummyList.add(ToDoItem("One", 1512224806372L, 0, ToDoItem.Priority.HIGH))
        dummyList.add(ToDoItem("Two", 1512224855096L, 0, ToDoItem.Priority.MEDIUM))
        dummyList.add(ToDoItem("Three", 1512224839258L, 0, ToDoItem.Priority.LOW))
    }

    private fun onItemChecked(item: ToDoItem) {
        dummyList.remove(item)
        adapter.updateList(dummyList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_past, menu)
        return true
    }
}
