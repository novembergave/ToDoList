package com.novembergave.todolist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
    private var itemList: MutableList<ToDoItem> = ArrayList()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_past, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.view_completed) {
            startActivity(Intent(this, CompletedActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        // set up fab
        fab_button.setOnClickListener {
            val pop = AddDialog()
            val fm = this@MainActivity.fragmentManager
            pop.show(fm, "add")
        }

        // load list saved in Room
        loadList()

        // initialise recyclerview
        linearLayoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter(itemList, this::onItemChecked)
        recyclerview.adapter = adapter

    }

    private fun onItemChecked(item: ToDoItem) {
        itemList.remove(item)
        adapter.updateList(itemList)
        // show empty placeholder if there are no items left in the list
        if (itemList.isEmpty()) {
            recyclerview.visibility = View.GONE
            empty_placeholder_view.visibility = View.VISIBLE
        }
        markItemAsDone(item)

    }

    private fun markItemAsDone(item: ToDoItem) {
        // add a completed time stamp and update item in Room
        val entity = ToDoEntity(item.id, item.title, item.dateAdded, getCurrentDate(), item.priority.toString())
        Single.fromCallable { roomToDoDatabase.toDoDao().updateItem(entity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun addItem(item: ToDoItem) {
        // add item to Room
        val databaseItem = ToDoEntity(item.id, item.title, item.dateAdded, item.dateCompleted, item.priority.toString())
        Single.fromCallable { roomToDoDatabase.toDoDao().createItem(databaseItem) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun loadList() {
        roomToDoDatabase.toDoDao().findAllOutstanding()
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { results ->
                    itemList = convertToDoEntityListToToDo(results)
                    adapter.updateList(itemList)
                    // clear the empty placeholder if there are items returned
                    if (results.isNotEmpty()) {
                        recyclerview.visibility = View.VISIBLE
                        empty_placeholder_view.visibility = View.GONE
                    }
                }
    }

    private fun convertToDoEntityListToToDo(list: List<ToDoEntity>): MutableList<ToDoItem> {
        val newList: MutableList<ToDoItem> = ArrayList()
        // iterate through the list
        list.mapTo(newList) { it -> ToDoItem(it.id, it.title, it.dateAdded, it.dateCompleted, ToDoItem.Priority.valueOf(it.priority)) }
        return newList
    }
}
