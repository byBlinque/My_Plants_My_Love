package com.example.myplantsmylove

import android.text.Editable
import java.time.LocalDate

class PlantAction(


    var id: Int = -1,

    // Id растения, которому принадлежит действие
    var plantId: Int = -1,

    // дата создания
    creationDate: String = "",

    // Название действия - полив, пересадка, обработка растения
    var name: String = "",

    // Заметка о действии
    var note: String = "",

    // 0 - Каждый день/месяц/год
    // 1 - По определенным дням недели
    // 2 - В конкретный день/дни
    var actionDateType: Int = 0,

    // 1 - временной промежуток
            // 0 - каждый день
            // 1 - каждый месяц
            // 2 - каждый год
    var everyTimeInterval: Int = 1,

    // если выбрали временной промежуток через месяц, то определенное число каждого месяца будет храниться здесь
    var certainDateOfMonth: Int = -1,

    // если выбрали временной промежуток через год, то определенный день в году будет храниться здесь
    var certainDateOfYear: String = "",

    // 2 - дни недели, по которым нужно выполнять действие
    var weekDaysSchedule: String = "!0000000", // <- первый элемент пустой

    // 3 - промежуток, в течение которого нужно выполнять действие - начало и конец
    //var dateRange: Array<LocalDate> = arrayOf(LocalDate.parse("2022-01-01"), LocalDate.parse("2022-01-02")),
    //
    var certainDate1: String = "",
    var certainDate2: String = "",

    // Цвет действия, который будет отображаться на календаре
    var color: String = "#FF0000"

): java.io.Serializable {
    companion object {
        var dateTypeList = arrayOf<String>(
            "каждый день/месяц/год",
            "по определенным дням недели",
            "в конкретный день/дни",
        )

        var timeIntervalList = arrayOf<String>(
            "каждый день",
            "каждый месяц",
            "каждый год"
        )

        fun parseDateType(dateTypeString: String): Int {
            var result: Int = -1
            for (dateType in dateTypeList) {
                if (dateType.toString().equals(dateTypeString)) {
                    result = dateTypeList.indexOf(dateType)
                }
            }
            return result
        }
        fun intToActionDateType(actionTypeInt: Int): String
        {
            return dateTypeList.get(actionTypeInt)
        }

        fun parseTimeInterval(timeIntervalString: String): Int {
            var result: Int = -1
            for (dateTime in timeIntervalList) {
                if (dateTime.toString().equals(timeIntervalString)) {
                    result = timeIntervalList.indexOf(dateTime)
                }
            }
            return result
        }
        fun intToActionDateTimeInterval(actionTimeIntervalInt: Int): String
        {
            return timeIntervalList.get(actionTimeIntervalInt)
        }

    }



}