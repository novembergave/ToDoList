package com.novembergave.todolist

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.novembergave.todolist.provider.TaskContentProvider
import com.novembergave.todolist.room.COLUMN_NAME
import com.novembergave.todolist.room.ToDoDatabase
import com.novembergave.todolist.room.ToDoEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), AddDialog.SaveNewToDoItem {

    const private val LOADER_CHEESES = 1

    @Inject
    lateinit var roomToDoDatabase: ToDoDatabase
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter
    private var itemList: MutableList<ToDoItem> = ArrayList()
    private lateinit var fabButton: FloatingActionButton

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

        fabButton = findViewById(R.id.fab_button)
        fabButton.setOnClickListener {
            val pop = AddDialog()
            val fm = this@MainActivity.fragmentManager
            pop.show(fm, "name")
        }

//        loadList()
        val loader = supportLoaderManager.initLoader(LOADER_CHEESES, null, mLoaderCallbacks)

        linearLayoutManager = LinearLayoutManager(this)
        completed_recyclerview.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter(this::onItemChecked)
        completed_recyclerview.adapter = adapter

    }

    private val mLoaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {

        override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
            when (id) {
                LOADER_CHEESES -> return CursorLoader(applicationContext,
                        TaskContentProvider.URI_CHEESE,
                        arrayOf(COLUMN_NAME), null, null, null)
                else -> throw IllegalArgumentException()
            }
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
            when (loader.id) {
                LOADER_CHEESES -> adapter.setCheeses(data)
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            when (loader.id) {
                LOADER_CHEESES -> adapter.setCheeses(null)
            }
        }

    }

    private fun onItemChecked(item: ToDoItem) {
        itemList.remove(item)
//        adapter.setCheeses(itemList)
        markItemAsDone(item)

    }

    private fun markItemAsDone(item: ToDoItem) {
        val stringId = item.id.toString()
        var uri = TaskContentProvider.URI_CHEESE
        uri = uri.buildUpon().appendPath(stringId).build()
        contentResolver.update(uri, null, null)
        supportLoaderManager.restartLoader(LOADER_CHEESES, null, mLoaderCallbacks)
//        val entity = ToDoEntity(item.id, item.title, item.dateAdded, getCurrentDate(), item.priority.toString())
//        Single.fromCallable { roomToDoDatabase.toDoDao().updateItem(entity) }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
    }

    override fun addItem(item: ToDoItem) {
        addItemToDb(item)
    }

    private fun addItemToDb(item: ToDoItem) {
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
                }
    }

    private fun convertToDoEntityListToToDo(list: List<ToDoEntity>): MutableList<ToDoItem> {
        val newList: MutableList<ToDoItem> = ArrayList()
        for (it in list) {
            newList.add(ToDoItem(it.id, it.title, it.dateAdded, it.dateCompleted, ToDoItem.Priority.valueOf(it.priority)))
        }
        return newList
    }
}
