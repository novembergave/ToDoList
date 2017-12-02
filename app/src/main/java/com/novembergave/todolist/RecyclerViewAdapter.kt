package com.novembergave.todolist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.novembergave.todolist.utils.CompareOutstandingToDos
import com.novembergave.todolist.utils.inflate


class RecyclerViewAdapter(private var list: List<ToDoItem>, private var changeListener: (ToDoItem) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindTo(list[position], changeListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(parent.inflate(R.layout.list_item))

    fun updateList(list: List<ToDoItem>) {
        val sortedList = list.sortedWith(CompareOutstandingToDos)
        this.list = sortedList
        notifyDataSetChanged()
    }

}