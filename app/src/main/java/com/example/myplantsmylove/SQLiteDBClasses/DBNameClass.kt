package com.example.myplantsmylove.SQLiteDBClasses

import android.provider.BaseColumns

object DBNameClass {
    class DBEntry: BaseColumns {
        companion object {
            const val PLANT_TABLE_NAME = "plant_table"
            const val PLANT_COLUMN_NAME = "name"
            const val PLANT_COLUMN_NOTE = "note"
            const val PLANT_COLUMN_LOCATION = "location"
            const val PLANT_COLUMN_DESCRIPTION = "description"
            const val PLANT_COLUMN_CREATION_DATE = "creation_date"

            const val DATABASE_VERSION = 2
            const val DATABASE_NAME = "plant.db"

            const val SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS $PLANT_TABLE_NAME (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "$PLANT_COLUMN_NAME TEXT," +
                        "$PLANT_COLUMN_NOTE TEXT)," +
                        "$PLANT_COLUMN_LOCATION TEXT)," +
                        "$PLANT_COLUMN_DESCRIPTION TEXT)," +
                        "$PLANT_COLUMN_CREATION_DATE TEXT)"

            const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $PLANT_TABLE_NAME"
        }
    }
}

