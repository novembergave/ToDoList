package com.novembergave.todolist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.novembergave.todolist.utils.CompareOutstandingToDos
import com.novembergave.todolist.utils.inflate

class CompletedViewAdapter(private var list: List<ToDoItem>) : RecyclerView.Adapter<CompletedViewHolder>() {

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CompletedViewHolder?, position: Int) {
        holder?.bindTo(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CompletedViewHolder(parent.inflate(R.layout.completed_list_item))

    fun updateList(list: List<ToDoItem>) {
        val sortedList = list.sortedWith(CompareOutstandingToDos)
        this.list = sortedList
        notifyDataSetChanged()
    }

}