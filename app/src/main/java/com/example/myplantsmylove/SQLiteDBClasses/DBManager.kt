package com.example.myplantsmylove.SQLiteDBClasses

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.myplantsmylove.Plant

class DBManager(context: Context) {
    val dbHelper = DBHelper(context)
    var db: SQLiteDatabase? = null

    fun openDB() {
        db = dbHelper.writableDatabase
    }
    fun String.insertToDB(
        plantName: String,
        plantNote: String,
        plantLocation: String,
        plantDescription: String,
        plantCreationDate: String
    ) {
        val values = ContentValues().apply {
            put(DBNameClass.DBEntry.PLANT_COLUMN_NAME, plantName)
            put(DBNameClass.DBEntry.PLANT_COLUMN_NOTE, plantNote)
            put(DBNameClass.DBEntry.PLANT_COLUMN_LOCATION, plantLocation)
            put(DBNameClass.DBEntry.PLANT_COLUMN_DESCRIPTION, plantDescription)
            put(DBNameClass.DBEntry.PLANT_COLUMN_CREATION_DATE, plantCreationDate)
        }
        db?.insert(DBNameClass.DBEntry.PLANT_TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun readDBData(): ArrayList<String> {
        var plantList = ArrayList<String>()
        val cursor = db?.query(DBNameClass.DBEntry.PLANT_TABLE_NAME, null, null, null, null, null, null)

            while(cursor?.moveToNext()!!) {
                val dataText = cursor.getString(cursor.getColumnIndex(DBNameClass.DBEntry.PLANT_COLUMN_NAME))
                plantList.add(dataText.toString())
            }

        return plantList
    }

}