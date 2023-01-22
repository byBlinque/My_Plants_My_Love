package com.example.myplantsmylove.SQLiteDBClasses

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myplantsmylove.Plant
import com.example.myplantsmylove.PlantAction

class DBHelper(context: android.content.Context) :
    SQLiteOpenHelper(context, "plant_db", null, 8) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS plant_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "creation_date TEXT," +
                "name TEXT," +
                "note TEXT," +
                "location TEXT," +
                "description TEXT)")

        db?.execSQL("CREATE TABLE IF NOT EXISTS plant_action_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "plant_id TEXT," +
                "creation_date TEXT," +
                "name TEXT," +
                "note TEXT," +
                "action_date_type INTEGER," +
                "every_time_interval INTEGER," +
                "certain_date_of_month INTEGER," +
                "certain_date_of_year INTEGER," +
                "week_day_schedule TEXT," +
                "certain_date_1 TEXT," +
                "certain_date_2 TEXT," +
                "color TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS plant_table")
        db?.execSQL("DROP TABLE IF EXISTS plant_action_table")
        onCreate(db)
    }


    fun insertPlantToDB(creationDate: String, name: String, note: String, location: String, description: String): Long {

        var db = this.writableDatabase

        val values = ContentValues()

        values.put("creation_date", creationDate)
        values.put("name", name)
        values.put("note", note)
        values.put("location", location)
        values.put("description", description)

        val success = db.insert("plant_table", null, values)

        db.close()
        return success
    }

    fun insertPlantActionToDB(plantId: Int,
                              creationDate: String,
                              name: String,
                              note: String,
                              actionDateType: Int,
                              everyTimeInterval: Int,
                              certainDateOfMonth: Int,
                              certainDateOfYear: String,
                              weekDaySchedule: String,
                              certainDate1: String,
                              certainDate2: String,
                              color: String): Long {

        var db = this.writableDatabase

        val values = ContentValues()

        values.put("plant_id", plantId)
        values.put("creation_date", creationDate)
        values.put("name", name)
        values.put("note", note)
        values.put("action_date_type", actionDateType)
        values.put("every_time_interval", everyTimeInterval)
        values.put("certain_date_of_month", certainDateOfMonth)
        values.put("certain_date_of_year", certainDateOfYear)
        values.put("week_day_schedule", weekDaySchedule)
        values.put("certain_date_1", certainDate1)
        values.put("certain_date_2", certainDate2)
        values.put("color", color)

        val success = db.insert("plant_action_table", null, values)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getPlantData(): ArrayList<Plant> {

        val plantList = ArrayList<Plant>()
        val db = writableDatabase
        val selectALLQuery = "SELECT * FROM plant_table"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor.moveToFirst()) {
            do {
                plantList.add(Plant(
                    id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                    creationDate = cursor.getString(cursor.getColumnIndex("creation_date")),
                    name = cursor.getString(cursor.getColumnIndex("name")),
                    note = cursor.getString(cursor.getColumnIndex("note")),
                    location = cursor.getString(cursor.getColumnIndex("location")),
                    description = cursor.getString(cursor.getColumnIndex("description"))
                ))

                //plants.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return plantList
    }

    @SuppressLint("Range")
    fun getPlantActionData(): ArrayList<PlantAction> {

        val plantActionList = ArrayList<PlantAction>()
        val db = writableDatabase
        val selectALLQuery = "SELECT * FROM plant_action_table"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor.moveToFirst()) {
            do {
                plantActionList.add(PlantAction(
                    id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                    plantId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("plant_id"))),
                    creationDate = cursor.getString(cursor.getColumnIndex("creation_date")),
                    name = cursor.getString(cursor.getColumnIndex("name")),
                    note = cursor.getString(cursor.getColumnIndex("note")),
                    actionDateType = Integer.parseInt(cursor.getString(cursor.getColumnIndex("action_date_type"))),
                    everyTimeInterval = Integer.parseInt(cursor.getString(cursor.getColumnIndex("every_time_interval"))),
                    certainDateOfMonth = Integer.parseInt(cursor.getString(cursor.getColumnIndex("certain_date_of_month"))),
                    certainDateOfYear = cursor.getString(cursor.getColumnIndex("certain_date_of_year")),
                    weekDaysSchedule = cursor.getString(cursor.getColumnIndex("week_day_schedule")),
                    certainDate1 = cursor.getString(cursor.getColumnIndex("certain_date_1")),
                    certainDate2 = cursor.getString(cursor.getColumnIndex("certain_date_2")),
                    color = cursor.getString(cursor.getColumnIndex("color"))
                ))

                //plants.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return plantActionList
    }

    @SuppressLint("Range")
    fun getSelectedPlantActionData(plantId: Int): ArrayList<PlantAction> {

        val plantActionList = ArrayList<PlantAction>()
        val db = writableDatabase
        val selectALLQuery = "SELECT * FROM plant_action_table WHERE plant_id LIKE $plantId"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor.moveToFirst()) {
            do {
                plantActionList.add(PlantAction(
                    id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                    plantId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("plant_id"))),
                    creationDate = cursor.getString(cursor.getColumnIndex("creation_date")),
                    name = cursor.getString(cursor.getColumnIndex("name")),
                    note = cursor.getString(cursor.getColumnIndex("note")),
                    actionDateType = Integer.parseInt(cursor.getString(cursor.getColumnIndex("action_date_type"))),
                    everyTimeInterval = Integer.parseInt(cursor.getString(cursor.getColumnIndex("every_time_interval"))),
                    certainDateOfMonth = Integer.parseInt(cursor.getString(cursor.getColumnIndex("certain_date_of_month"))),
                    certainDateOfYear = cursor.getString(cursor.getColumnIndex("certain_date_of_year")),
                    weekDaysSchedule = cursor.getString(cursor.getColumnIndex("week_day_schedule")),
                    certainDate1 = cursor.getString(cursor.getColumnIndex("certain_date_1")),
                    certainDate2 = cursor.getString(cursor.getColumnIndex("certain_date_2")),
                    color = cursor.getString(cursor.getColumnIndex("color"))
                ))

                //plants.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return plantActionList
    }

    fun deletePlant(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete("plant_table", "id=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deletePlantAction(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete("plant_action_table", "id=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}