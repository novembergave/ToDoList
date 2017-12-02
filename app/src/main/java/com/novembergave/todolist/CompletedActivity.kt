package com.novembergave.todolist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.novembergave.todolist.room.ToDoDatabase
import com.novembergave.todolist.room.ToDoEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
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
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item?.itemId == R.id.delete_forever) {
            deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)
        App.component.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.completed_activity)

        loadList()

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
                }
    }

    private fun convertToDoEntityListToToDo(list: List<ToDoEntity>): MutableList<ToDoItem> {
        val newList: MutableList<ToDoItem> = ArrayList()
        list.forEach {
            newList.add(ToDoItem(it.id, it.title, it.dateAdded, it.dateCompleted, ToDoItem.Priority.valueOf(it.priority)))
        }
        return newList
    }

    private fun deleteAll() {
        itemList.forEach {
            val entity = ToDoEntity(it.id, it.title, it.dateAdded, it.dateCompleted, it.priority.toString())
            Single.fromCallable { roomToDoDatabase.toDoDao().deleteItem(entity) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
        itemList.clear()
        adapter.updateList(itemList)
    }
}