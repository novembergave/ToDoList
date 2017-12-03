package com.novembergave.todolist

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.novembergave.todolist.room.ToDoDatabase
import com.novembergave.todolist.room.ToDoEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_completed.*
import javax.inject.Inject


class CompletedActivity : AppCompatActivity() {

    @Inject
    lateinit var roomToDoDatabase: ToDoDatabase
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CompletedViewAdapter
    private var itemList: MutableList<ToDoItem> = ArrayList()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == android.R.id.home -> {
                onBackPressed()
                true
            }
            item?.itemId == R.id.delete_forever -> {
                showDeleteAlert()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)
        App.component.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.completed_activity)

        // load data from Room
        loadList()

        // initialise the recyclerView
        linearLayoutManager = LinearLayoutManager(this)
        completed_recyclerview.layoutManager = linearLayoutManager
        adapter = CompletedViewAdapter(itemList)
        completed_recyclerview.adapter = adapter
    }

    private fun loadList() {
        roomToDoDatabase.toDoDao().findAllCompleted()
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { results ->
                    itemList = convertToDoEntityListToToDo(results)
                    adapter.updateList(itemList)
                    // remove the empty placeholder if there are items returned in list
                    if (results.isNotEmpty()) {
                        completed_recyclerview.visibility = View.VISIBLE
                        completed_image.visibility = View.GONE
                        completed_text.visibility = View.GONE
                    }
                }
    }

    private fun convertToDoEntityListToToDo(list: List<ToDoEntity>): MutableList<ToDoItem> {
        // iterate through list and convert
        val newList: MutableList<ToDoItem> = ArrayList()
        list.forEach {
            newList.add(ToDoItem(it.id, it.title, it.dateAdded, it.dateCompleted, ToDoItem.Priority.valueOf(it.priority)))
        }
        return newList
    }

    private fun showDeleteAlert() {
        // Only show alert if there's something to delete
        if (itemList.isNotEmpty()) {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage(getString(R.string.delete_all))
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), { _, _ ->
                deleteAll()
            })
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), { dialogInterface, _ ->
                dialogInterface.cancel()
            })
            alertDialog.show()
        }
    }

    private fun deleteAll() {
        // iterate through the completed item list and delete
        itemList.forEach {
            val entity = ToDoEntity(it.id, it.title, it.dateAdded, it.dateCompleted, it.priority.toString())
            Single.fromCallable { roomToDoDatabase.toDoDao().deleteItem(entity) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
        // clear the list stored in the global variable
        itemList.clear()
        // update that UI
        adapter.updateList(itemList)
        completed_recyclerview.visibility = View.GONE
        completed_image.visibility = View.VISIBLE
        completed_text.visibility = View.VISIBLE
    }
}