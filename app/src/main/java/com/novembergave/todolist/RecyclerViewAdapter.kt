package com.novembergave.todolist

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.novembergave.todolist.room.*


//class RecyclerViewAdapter(private var list: List<ToDoItem>, private var changeListener: (ToDoItem) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {
//
//    override fun getItemCount() = list.size
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        holder.bindTo(list[position], changeListener)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(parent.inflate(R.layout.list_item))
//
//    fun updateList(list: List<ToDoItem>) {
//        val sortedList = list.sortedWith(CompareOutstandingToDos)
//        this.list = sortedList
//        notifyDataSetChanged()
//    }
//
//}

class RecyclerViewAdapter(private var changeListener: (ToDoItem) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (mCursor!!.moveToPosition(position)) {
            val toDoItem = ToDoItem(
                    mCursor!!.getLong(mCursor!!.getColumnIndexOrThrow(COLUMN_ID)),
                    mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(COLUMN_NAME)),
                    mCursor!!.getLong(mCursor!!.getColumnIndexOrThrow(COLUMN_ADDED)),
                    mCursor!!.getLong(mCursor!!.getColumnIndexOrThrow(COLUMN_DONE)),
                    ToDoItem.Priority.valueOf(mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(COLUMN_PRIORITY))))
            holder.bindTo(toDoItem, changeListener)
        }
    }

    override fun getItemCount(): Int {
        return if (mCursor == null) 0 else mCursor!!.count
    }

    internal fun setCheeses(cursor: Cursor?) {
        mCursor = cursor
        notifyDataSetChanged()
    }

}