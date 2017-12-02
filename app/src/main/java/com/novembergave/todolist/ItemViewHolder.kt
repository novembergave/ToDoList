package com.novembergave.todolist

import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var view = v

    fun bindTo(item: ToDoItem, listener: (ToDoItem) -> Unit) {
        view.item_title.text = item.title
        view.item_date.text = setCurrentDate(item.dateAdded)
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
        // clears the view holder so that when it is reused it is not checked
        view.item_check_box.setOnCheckedChangeListener(null)
        view.item_check_box.isChecked = false
        view.item_check_box.setOnCheckedChangeListener { _, _ ->
            listener(item)
        }
    }

    private fun setCurrentDate(date: Long): String {
        val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

}