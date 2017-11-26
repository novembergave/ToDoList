package com.novembergave.todolist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


class RecyclerViewAdapter (list: List<ToDoItem>) : RecyclerView.Adapter<ItemViewHolder>() {

    private var list:List<ToDoItem> = list

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindTo(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(parent.inflate(R.layout.list_item))


    fun updateList(list: List<ToDoItem>) {
        this.list = list
    }

}