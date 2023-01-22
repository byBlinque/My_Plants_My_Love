package com.example.myplantsmylove

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplantsmylove.SQLiteDBClasses.DBHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var dateText: TextView
    private lateinit var plantDialogBtn: Button
    private lateinit var currentDate: LocalDate
    private lateinit var formatter: DateTimeFormatter
    private lateinit var formattedDate: String

    lateinit var layoutManager: RecyclerView.LayoutManager

    var plantArray: ArrayList<Plant> = arrayListOf()

    lateinit var plantRV: RecyclerView
    lateinit var plantAdapter: PlantAdapter

    var currentDayOfWeek: String = ""
    var currentDaysInMonth: Int = 0

    var savedDay: Int = 0
    var savedMonth: Int = 0
    var savedYear: Int = 0

    // что делать с полученной датой - записать в новый объект, поменять в бд и пр.
    // 0 - начальное значение / ничего не делать, 1 - записать дату в созданное действие, 2 - поменять дату в существующем действии
    var dateActionType: Int = 0

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        dateText = findViewById<TextView>(R.id.text_view_date)
        plantDialogBtn = findViewById<Button>(R.id.plant_dialog_btn)

        val db = DBHelper(this)

        //db.insertPlantToDB("creation date", "name", "note", "location", "description")
        //db.insertPlantToDB("creation date", "name", "note", "location", "description")
        //db.deletePlant(14)

        plantArray = db.getPlantData()
        dateText.text = ""

        for (plant in plantArray) {
            dateText.text = dateText.text.toString() + " ${plant.id.toString()}"
        }

        plantRV = findViewById(R.id.plant_rv)
        plantAdapter = PlantAdapter(this, plantArray)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        plantRV.layoutManager = layoutManager
        plantRV.adapter = plantAdapter


        dateText.setOnClickListener {
            dateActionType = 1
            getCurrentDateTime()
            //DatePickerDialog(this, this, currentDate.year, monthForCalendar(currentDate.monthValue.toInt()), currentDate.dayOfMonth).show()

            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // обрабатываем нажатие на кнопку добавления растения
        plantDialogBtn.setOnClickListener {

            // надуваем xml диалога добавления растения и показываем его, инициализируем все его элементы
            var dialogView = layoutInflater.inflate(R.layout.add_plant_dialog, null)

            var addPlantDialog = Dialog(this)
            addPlantDialog.setContentView(dialogView)

            addPlantDialog.setCancelable(true)
            addPlantDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addPlantDialog.show()

            val window: Window? = addPlantDialog.getWindow()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            var addPlantBtn: Button = dialogView.findViewById(R.id.add_plant_btn)
            var cancelPlantBtn: Button = dialogView.findViewById(R.id.cancel_plant_btn)
            var plantNameET: EditText = dialogView.findViewById(R.id.plant_name_et)
            var plantNoteET: EditText = dialogView.findViewById(R.id.plant_note_et)
            var plantLocationET: EditText = dialogView.findViewById(R.id.plant_location_et)
            var plantDescriptionET: EditText = dialogView.findViewById(R.id.plant_description_et)

            // обрабатываем нажатия на кнопки добавления растения и отмены
            addPlantBtn.setOnClickListener {
                var success = db.insertPlantToDB(LocalDate.now().toString(), plantNameET.text.toString(), plantNoteET.text.toString(), plantLocationET.text.toString(), plantDescriptionET.text.toString())
                if (success.toInt() == -1) {
                    Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show();
                    addPlantDialog.dismiss()
                }
                else {
                    updatePlantRV(db)
                    addPlantDialog.dismiss()
                }

            }

            cancelPlantBtn.setOnClickListener {
                addPlantDialog.dismiss()
            }

            //db.insertPlantToDB(LocalDate.now().toString(), "Монстерка", "На продажу весной", "Полка 2", "")
            //updatePlantRV(db)

        }

}

    // обновляем адаптер recycler view для оторажения списка растения
    fun updatePlantRV(db: DBHelper) {
        plantArray = db.getPlantData()
        plantAdapter = PlantAdapter(this, plantArray)
        plantRV.adapter = plantAdapter
    }

    // получаем сегодняшнюю дату и день недели
    public fun getCurrentDateTime() {
        currentDate = LocalDate.now()
        formattedDate = currentDate.format(formatter)

        currentDayOfWeek = dayOfWeekToRus(currentDate.dayOfWeek.toString())
    }

    private fun dayOfWeekToRus(dayOfWeek: String): String {
        when (dayOfWeek) {
            "MONDAY" -> return "Понедельник"
            "WEDNESDAY" -> return "Вторник"
            "TUESDAY" -> return "Среда"
            "THURSDAY" -> return "Четверг"
            "FRIDAY" -> return "Пятница"
            "SATURDAY" -> return "Суббота"
            "SUNDAY" -> return "Воскресенье"
            else -> {
                return "Неизвестный день недели"
            }
        }
    }

    private fun showDate(view: TextView, year: Int, month: Int, day: Int) {
        view.setText(day.toString() + "-" + monthForCalendar(month).toString() + "-" + year.toString())
    }

    // отсчет месяца начинается с нуля, нужно увеличить на 1 перед тем, как показать
    fun monthForCalendar(monthValue: Int): Int {
        return monthValue - 1
    }
    fun monthFromCalendar(monthValue: Int): Int {
        return monthValue + 1
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = monthFromCalendar(month)
        savedYear = year

        when(dateActionType) {
            1 -> showDate(dateText, savedYear, savedMonth, savedDay)
            2 -> showDate(dateText, 2, 1, 2)
            3 -> showDate(dateText, 3, 2, 3)
            else -> dateActionType = 0
        }
    }

}



