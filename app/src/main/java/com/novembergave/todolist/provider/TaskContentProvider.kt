package com.novembergave.todolist.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.novembergave.todolist.room.TABLE_NAME
import com.novembergave.todolist.room.ToDoDatabase
import com.novembergave.todolist.room.ToDoEntity
import java.util.*


class TaskContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code = MATCHER.match(uri)
        if (code == CODE_CHEESE_DIR || code == CODE_CHEESE_ITEM) {
            val context = context ?: return null
            val cheese = ToDoDatabase.getInstance(context).cheese()
            val cursor: Cursor
            if (code == CODE_CHEESE_DIR) {
                cursor = cheese.selectAll()
            } else {
                cursor = cheese.selectById(ContentUris.parseId(uri))
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    override fun getType(uri: Uri): String? {
        when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME
            CODE_CHEESE_ITEM -> return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> {
                val context = context ?: return null
                val id = ToDoDatabase.getInstance(context).cheese()
                        .insert(ToDoEntity.fromContentValues(values))
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
            CODE_CHEESE_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri)
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    override fun delete(uri: Uri, selection: String?,
                        selectionArgs: Array<String>?): Int {
        when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID" + uri)
            CODE_CHEESE_ITEM -> {
                val context = context ?: return 0
                val count = ToDoDatabase.getInstance(context).cheese()
                        .deleteById(ContentUris.parseId(uri))
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID" + uri)
            CODE_CHEESE_ITEM -> {
                val context = context ?: return 0
                val cheese = ToDoEntity.fromContentValues(values)
                cheese.id = ContentUris.parseId(uri)
                val count = ToDoDatabase.getInstance(context).cheese()
                        .update(cheese)
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    override fun applyBatch(
            operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult?> {
        val context = context ?: return arrayOfNulls(0)
        val database = ToDoDatabase.getInstance(context)
        database.beginTransaction()
        try {
            val result = super.applyBatch(operations)
            database.setTransactionSuccessful()
            return result
        } finally {
            database.endTransaction()
        }
    }

    override fun bulkInsert(uri: Uri, valuesArray: Array<ContentValues>): Int {
        when (MATCHER.match(uri)) {
            CODE_CHEESE_DIR -> {
                val context = context ?: return 0
                val database = ToDoDatabase.getInstance(context)
                val cheeses = arrayOfNulls<ToDoEntity>(valuesArray.size)
                for (i in valuesArray) {
                    cheeses[i.size()] = ToDoEntity.fromContentValues(valuesArray[i.size()])
                }
                return database.cheese().insertAll(cheeses).size
            }
            CODE_CHEESE_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri)
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
    }

    companion object {

        const val AUTHORITY = "com.example.android.contentprovidersample.provider"

        val URI_CHEESE = Uri.parse(
                "content://" + AUTHORITY + "/" + TABLE_NAME)

        const private val CODE_CHEESE_DIR = 1

        const private val CODE_CHEESE_ITEM = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_CHEESE_DIR)
            MATCHER.addURI(AUTHORITY, TABLE_NAME + "/*", CODE_CHEESE_ITEM)
        }
    }

}