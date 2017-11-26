package com.novembergave.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner


class AddDialog : DialogFragment(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        var selectedText = p0!!.getItemAtPosition(p2)
    }

    private lateinit var spinner: Spinner

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        val view = inflater.inflate(R.layout.add_item_fragment, null)

        spinner = view.findViewById(R.id.spinner)

        val listOfItems = resources.getStringArray(R.array.priority_levels)
        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listOfItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        builder
                .setTitle(R.string.add_item)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, { d, _ ->
                    d.cancel()
                })
                .setPositiveButton(android.R.string.ok, { d, _ ->
                    d.cancel()
                })
        // Create the AlertDialog object and return it
        return builder.create()
    }
}