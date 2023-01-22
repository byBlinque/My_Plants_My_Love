package com.example.myplantsmylove

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CalendarActivity : AppCompatActivity() {


    lateinit var selectedDate: LocalDate
    lateinit var calendarMonthYearTV: TextView
    lateinit var calendarBackMonthTV: Button
    lateinit var calendarForwardMonthTV: Button
    lateinit var calendarRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarMonthYearTV = findViewById(R.id.calendar_month_year_tv)
        calendarBackMonthTV = findViewById(R.id.calendar_back_month_btn)
        calendarForwardMonthTV = findViewById(R.id.calendar_forward_month_btn)

        selectedDate = LocalDate.now()

        setMonthView()

        calendarBackMonthTV.setOnClickListener {
            previousMonthAction()
        }
        calendarForwardMonthTV.setOnClickListener {
            nextMonthAction()
        }
    }


    fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    fun setMonthView() {
        //calendarMonthYearTV.text = monthYearFromDate(selectedDate)
        calendarMonthYearTV.text = monthInRussian(selectedDate.month.value.toInt()) + " " + selectedDate.year

        var daysInMonth: ArrayList<String> = daysInMonthArray(selectedDate)

        calendarRecyclerView = findViewById(R.id.calendar_recycler_view)

        var layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext,7)
        calendarRecyclerView.layoutManager = layoutManager

        var calendarAdapter: CalendarAdapter = CalendarAdapter(this, daysInMonth)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        var daysInMonthArray: ArrayList<String> = arrayListOf()
        var yearMonth: YearMonth = YearMonth.of(date.year, date.month)
        var daysInMonthLength: Int = yearMonth.lengthOfMonth()

        var firstOfMonth: LocalDate = date.withDayOfMonth(1)
        var dayOfWeek: Int = firstOfMonth.dayOfWeek.value.toInt()

        for (i in 2..43) {
            if (i <= dayOfWeek || i > daysInMonthLength + dayOfWeek) {
                daysInMonthArray.add("")
            }
            else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    fun monthYearFromDate(date: LocalDate): String {
        var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-yyyy")
        return date.format(formatter).toString()
    }

    fun monthInRussian(monthValue: Int): String {
        val monthNames = arrayOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )
        return monthNames[monthValue-1]
    }


}