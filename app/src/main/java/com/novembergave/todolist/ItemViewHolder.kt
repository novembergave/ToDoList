package com.novembergave.todolist

import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item.view.*


class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var view = v

    fun bindTo(item: ToDoItem) {
        view.item_title.text = item.title
        view.item_date.text = item.date
        val backgroundColor = when {
                    item.priority == ToDoItem.Priority.HIGH ->
                        getColor(view.context, R.color.priorityHigh)
                    item.priority == ToDoItem.Priority.MEDIUM ->
                        getColor(view.context, R.color.priorityMedium)
                    item.priority == ToDoItem.Priority.LOW ->
                        getColor(view.context, R.color.priorityLow)
                    else -> getColor(view.context, R.color.priorityLow)
                }

        view.item_holder.setBackgroundColor(backgroundColor)
    }

}