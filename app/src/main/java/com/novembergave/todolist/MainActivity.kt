package com.novembergave.todolist

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.novembergave.todolist.room.ToDoDatabase
import com.novembergave.todolist.room.ToDoEntity
import com.novembergave.todolist.utils.getCurrentDate
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AddDialog.SaveNewToDoItem {

    @Inject
    lateinit var roomToDoDatabase: ToDoDatabase
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter
    private var dummyList: MutableList<ToDoItem> = ArrayList()
    private lateinit var fabButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        fabButton = findViewById(R.id.fab_button)
        fabButton.setOnClickListener {
            val pop = AddDialog()
            val fm = this@MainActivity.fragmentManager
            pop.show(fm, "name")
        }

        loadList()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter(dummyList, this::onItemChecked)
        recyclerview.adapter = adapter

    }

    private fun onItemChecked(item: ToDoItem) {
        dummyList.remove(item)
        adapter.updateList(dummyList)
        markItemAsDone(item)

    }

    private fun markItemAsDone(item: ToDoItem) {
        val entity = ToDoEntity(item.id, item.title, item.dateAdded, getCurrentDate(), item.priority.toString())
        Single.fromCallable { roomToDoDatabase.toDoDao().updateItem(entity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_past, menu)
        return true
    }

    override fun addItem(item: ToDoItem) {
        addItemToDb(item)
        dummyList.add(item)
        adapter.updateList(dummyList)
    }

    private fun addItemToDb(item: ToDoItem) {
        val databaseItem = ToDoEntity(item.id, item.title, item.dateAdded, item.dateCompleted, item.priority.toString())

        Single.fromCallable { roomToDoDatabase.toDoDao().createItem(databaseItem) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun loadList() {
        roomToDoDatabase.toDoDao().findAll()
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { results ->
                    dummyList = convertToDoEntityListToToDo(results)
                    adapter.updateList(dummyList)
                }
    }

    private fun convertToDoEntityListToToDo(list: List<ToDoEntity>): MutableList<ToDoItem> {
        val newList: MutableList<ToDoItem> = ArrayList()
        list.forEach {
            if (it.dateCompleted!! == 0L) {
                val toDoItem = ToDoItem(it.id, it.title, it.dateAdded, it.dateCompleted, ToDoItem.Priority.valueOf(it.priority))
                newList.add(toDoItem)
            }
        }
        return newList
    }
}
