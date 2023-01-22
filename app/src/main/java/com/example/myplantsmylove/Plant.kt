package com.example.myplantsmylove

import java.time.LocalDate

data class Plant (
    var id: Int = -1,
    var creationDate: String?,
    var name: String?,
    var note: String?,
    var location: String?,
    var description: String = "",

    var plantActions: ArrayList<PlantAction> = arrayListOf()

    /*
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "note") var note: String = "",
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "creation_date") var creationDate: String = "",

    @Ignore var plantActions: ArrayList<PlantAction> = arrayListOf()
     */
): java.io.Serializable