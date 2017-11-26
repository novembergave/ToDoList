package com.novembergave.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.text.SimpleDateFormat
import java.util.*


class AddDialog : DialogFragment() {

    interface SaveNewToDoItem {
        fun addItem(item: ToDoItem)
    }

    private lateinit var spinner: Spinner
    private lateinit var titleTextInput: TextInputLayout
    private lateinit var prioritySelection: ToDoItem.Priority
    private lateinit var listener: SaveNewToDoItem

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.add_item_fragment, null)
        titleTextInput = view.findViewById(R.id.input_item_name_label)
        spinner = view.findViewById(R.id.spinner)
        val listOfItems = resources.getStringArray(R.array.priority_levels)
        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listOfItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                val selectedText = arg0.getItemAtPosition(position)
                prioritySelection = when (selectedText) {
                    getString(R.string.high) -> ToDoItem.Priority.HIGH
                    getString(R.string.medium) -> ToDoItem.Priority.MEDIUM
                    getString(R.string.low) -> ToDoItem.Priority.LOW
                    else -> ToDoItem.Priority.LOW
                }
            }
            override fun onNothingSelected(arg0: AdapterView<*>) {
                prioritySelection = ToDoItem.Priority.LOW
            }

        }


        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle(R.string.add_item)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, { d, _ ->
                    d.cancel()
                })
                .setPositiveButton(android.R.string.ok, { d, _ ->
                    listener.addItem(addItemToList())
                    d.dismiss()
                })
        // Create the AlertDialog object and return it
        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = when (context) {
            is SaveNewToDoItem -> context
            else -> throw ClassCastException(context.toString() + " must implement SaveNewToDoItem.")
        }
    }

    private fun addItemToList() = ToDoItem(getTitleToString(), getCurrentDate(), prioritySelection)

    private fun getTitleToString(): String {
        return titleTextInput.editText!!.text.toString()
    }

    private fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.getDefault())
        return simpleDateFormat.format(time)
    }

}