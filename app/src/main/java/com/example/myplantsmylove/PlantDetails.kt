package com.example.myplantsmylove

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplantsmylove.SQLiteDBClasses.DBHelper
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlantDetails : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    lateinit var plantIdTV: TextView
    lateinit var plantNameTV: TextView
    lateinit var plantNoteTV: TextView

    lateinit var plantActionDialogBtn: Button

    lateinit var plantActionRV: RecyclerView
    lateinit var plantActionAdapter: PlantActionAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var currentDate: LocalDate
    private lateinit var formattedDate: String
    var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    var dateInterval1: LocalDate = LocalDate.now()
    var dateInterval2: LocalDate = LocalDate.now()

    var currentDayOfWeek: String = ""
    var currentDaysInMonth: Int = 0

    var dateIntervalStep = 1

    var savedDay: Int = 0
    var savedMonth: Int = 0
    var savedYear: Int = 0

    var weekDaysString: String = "00000000"

    var plantArray: ArrayList<Plant> = arrayListOf()

    // создаем ОБЩИЙ массив действий растений
    var plantActionArray: ArrayList<PlantAction> = arrayListOf()

    var selectedPlantId: Int = -1
    var selectedPlantPosition: Int = -1

    //test var 1
    var count: Int = 0
    //test var 2
    var actionName: String = "Действие "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)

        // элементы активити - подробности о растении
        plantIdTV = findViewById(R.id.plant_id_tv)
        plantNameTV = findViewById(R.id.plant_name_tv)
        plantNoteTV = findViewById(R.id.plant_note_tv)
        plantActionDialogBtn = findViewById(R.id.plant_action_dialog_btn)

        selectedPlantPosition = intent.extras?.get("selectedPlantPosition") as Int
        selectedPlantId = intent.extras?.get("selectedPlantId") as Int

        // инициализируем базу данных и создаем массивы действий
        val db = DBHelper(this)
        plantArray = db.getPlantData()
        plantActionArray = db.getPlantActionData()

        // находим объект растения, который был выбран
        var selectedPlant: Plant? = plantArray.find { it.id == selectedPlantId }
        selectedPlant?.plantActions = db.getSelectedPlantActionData(selectedPlantId)

        // создаем recycler view для отображения действий растения
        plantActionRV = findViewById(R.id.plant_action_rv)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        plantActionRV.layoutManager = layoutManager
        if (selectedPlant != null) {
            plantActionAdapter = PlantActionAdapter(this, selectedPlant.plantActions)
        }
        plantActionRV.adapter = plantActionAdapter

        // отображаем подробности о растении
        plantIdTV.text = selectedPlantId.toString()
        plantNameTV.text = selectedPlant?.name.toString()
        plantNoteTV.text = selectedPlant?.note.toString()

        // вызываем диалог добавления действия
        plantActionDialogBtn.setOnClickListener {

            // надуваем xml диалога добавления действия, инициализируем все его элементы
            var dialogView = layoutInflater.inflate(R.layout.add_plant_action_dialog, null)

            var addPlantActionDialog = Dialog(this)
            addPlantActionDialog.setContentView(dialogView)

            addPlantActionDialog.setCancelable(true)
            addPlantActionDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addPlantActionDialog.show()

            val window: Window? = addPlantActionDialog.getWindow()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            // инициализируем название действия и заметку
            var plantActionNameACTV: AutoCompleteTextView = dialogView.findViewById(R.id.plant_action_name_actv)
            var plantActionNameAdapter = ArrayAdapter(this, R.layout.add_action_dropdown_menu_list_item, PlantAction.actionNameList)
            plantActionNameACTV.setAdapter(plantActionNameAdapter)

            var plantActionNoteET: EditText = dialogView.findViewById(R.id.plant_action_note_et)
            var addPlantActionBtn: Button = dialogView.findViewById(R.id.add_plant_action_btn)
            var cancelPlantBtn: Button = dialogView.findViewById(R.id.cancel_plant_action_btn)

            // инициализируем и обрабатываем выпадающее меню
            // 0 - Каждый день/месяц/год
            // 1 - По определенным дням недели
            // 2 - В конкретный день/дни
            var dateTypeArrayAdapter = ArrayAdapter(this, R.layout.add_action_dropdown_menu_list_item, PlantAction.dateTypeList)
            var dateTypeAutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.action_date_type_auto_complete_tv)
            dateTypeAutoCompleteTextView.setAdapter(dateTypeArrayAdapter)

            // 0 - Каждый день/месяц/год
                // 0 - каждый день
                // 1 - каждый месяц
                // 2 - каждый год
            var dateTimeIntervalAutoCompleteTextViewWrapper: TextInputLayout = dialogView.findViewById(R.id.date_time_interval_auto_complete_tv_wrapper)
            var dateTimeIntervalArrayAdapter = ArrayAdapter(this, R.layout.add_action_dropdown_menu_list_item, PlantAction.timeIntervalList)
            var dateTimeIntervalAutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.date_time_interval_auto_complete_tv)
            dateTimeIntervalAutoCompleteTextView.setAdapter(dateTimeIntervalArrayAdapter)

            // 0 - Каждый день/месяц/год
                // 1 - каждый месяц : здесь нужно будет выбрать конкретный день месяца
            var certainDateOfMonthAutoCompleteTextViewWrapper: TextInputLayout = dialogView.findViewById(R.id.certain_date_of_month_auto_complete_tv_wrapper)
            var certainDateOfMonthArrayAdapter = ArrayAdapter(this, R.layout.add_action_dropdown_menu_list_item, PlantAction.certainDateOfMonthList)
            var certainDateOfMonthAutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.certain_date_of_month_auto_complete_tv)
            certainDateOfMonthAutoCompleteTextView.setAdapter(certainDateOfMonthArrayAdapter)
                // 2 - каждый год : здесь нужно будет выбрать конкретный день в году
            var certainDateOfYearAutoCompleteTextViewWrapper: TextInputLayout = dialogView.findViewById(R.id.certain_date_of_year_auto_complete_tv_wrapper)
            var certainDateOfYearAutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.certain_date_of_year_auto_complete_tv)

            // 1 - По определенным дням недели
            var weekDaysButtons: FlexboxLayout = dialogView.findViewById(R.id.week_days_buttons)
            var monButton = dialogView.findViewById<MaterialCardView>(R.id.mon_button)
            var tueButton = dialogView.findViewById<MaterialCardView>(R.id.tue_button)
            var wenButton = dialogView.findViewById<MaterialCardView>(R.id.wen_button)
            var thuButton = dialogView.findViewById<MaterialCardView>(R.id.thu_button)
            var friButton = dialogView.findViewById<MaterialCardView>(R.id.fri_button)
            var satButton = dialogView.findViewById<MaterialCardView>(R.id.sat_button)
            var sunButton = dialogView.findViewById<MaterialCardView>(R.id.sun_button)

            // массив для ряда дней недели (!0000000) с пустым первым элементом
            var weekDaysButtonsArray = arrayListOf<Pair<MaterialCardView, Int>>(
                Pair(monButton, 0),
                Pair(monButton, 0),
                Pair(tueButton, 0),
                Pair(wenButton, 0),
                Pair(thuButton, 0),
                Pair(friButton, 0),
                Pair(satButton, 0),
                Pair(sunButton, 0)
            )

            // 2 - В конкретный день/дни
            var dateInterval1AutoCompleteTextViewWrapper: TextInputLayout = dialogView.findViewById(R.id.first_date_interval_auto_complete_tv_wrapper)
            var dateInterval1AutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.first_date_interval_auto_complete_tv)

            var dateInterval2AutoCompleteTextViewWrapper: TextInputLayout = dialogView.findViewById(R.id.second_date_interval_auto_complete_tv_wrapper)
            var dateInterval2AutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.second_date_interval_auto_complete_tv)

            // обрабатываем нажатия на элементы
                // тип даты действия
            dateTypeAutoCompleteTextView.setOnItemClickListener { parent, view, position, id -> plantActionDateTypeHandler(
                dateTypeAutoCompleteTextView.text.toString(),
                dateTimeIntervalAutoCompleteTextViewWrapper,
                certainDateOfMonthAutoCompleteTextViewWrapper,
                certainDateOfYearAutoCompleteTextViewWrapper,
                weekDaysButtons,
                dateInterval1AutoCompleteTextViewWrapper,
                dateInterval2AutoCompleteTextViewWrapper
            ) }
                // интервал выполнения действия
            dateTimeIntervalAutoCompleteTextView.setOnItemClickListener { parent, view, position, id -> plantActionDateIntervalHandler(
                dateTimeIntervalAutoCompleteTextView.text.toString(),
                certainDateOfMonthAutoCompleteTextViewWrapper,
                certainDateOfYearAutoCompleteTextViewWrapper)
            }
            certainDateOfYearAutoCompleteTextView.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker().build()
                datePicker.show(supportFragmentManager, "DatePicker")

                // Setting up the event for when ok is clicked
                datePicker.addOnPositiveButtonClickListener {
                    val date: Long = it
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    dateInterval1 = LocalDate.parse(format.format(date))
                    certainDateOfYearAutoCompleteTextView.setText(dateInterval1.format(formatter).toString())
                }

                // Setting up the event for when cancelled is clicked
                datePicker.addOnNegativeButtonClickListener {
                    //Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
                }

                // Setting up the event for when back button is pressed
                datePicker.addOnCancelListener {
                    //Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
                }
            }
                // кнопки дней недели
            monButton.setOnClickListener {
                weekDaysButtonsArray[1] = weekDayClick(1, monButton, weekDaysButtonsArray[1].second)
                changeWeekDayValue(1, weekDaysButtonsArray[1].second, weekDaysButtonsArray)
            }
            tueButton.setOnClickListener {
                weekDaysButtonsArray[2] = weekDayClick(2, tueButton, weekDaysButtonsArray[2].second)
            changeWeekDayValue(2, weekDaysButtonsArray[2].second, weekDaysButtonsArray)
            }
            wenButton.setOnClickListener {
                weekDaysButtonsArray[3] = weekDayClick(3, wenButton, weekDaysButtonsArray[3].second)
                changeWeekDayValue(3, weekDaysButtonsArray[3].second, weekDaysButtonsArray)
            }
            thuButton.setOnClickListener {
                weekDaysButtonsArray[4] = weekDayClick(4, thuButton, weekDaysButtonsArray[4].second)
                changeWeekDayValue(4, weekDaysButtonsArray[4].second, weekDaysButtonsArray)
            }
            friButton.setOnClickListener {
                weekDaysButtonsArray[5] = weekDayClick(5, friButton, weekDaysButtonsArray[5].second)
                changeWeekDayValue(5, weekDaysButtonsArray[5].second, weekDaysButtonsArray)
            }
            satButton.setOnClickListener {
                weekDaysButtonsArray[6] = weekDayClick(6, satButton, weekDaysButtonsArray[6].second)
                changeWeekDayValue(6, weekDaysButtonsArray[6].second, weekDaysButtonsArray)
            }
            sunButton.setOnClickListener {
                weekDaysButtonsArray[7] = weekDayClick(7, sunButton, weekDaysButtonsArray[7].second)
                changeWeekDayValue(7, weekDaysButtonsArray[7].second, weekDaysButtonsArray)
            }

            dateInterval1AutoCompleteTextView.setOnClickListener {
                //getCurrentDateTime()
                //DatePickerDialog(this, this, currentDate.year, PlantAction.monthForCalendar(currentDate.monthValue.toInt()), currentDate.dayOfMonth).show()

                val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
                datePicker.show(supportFragmentManager, "DatePicker")

                // Setting up the event for when ok is clicked
                datePicker.addOnPositiveButtonClickListener {
                    val startDate: Long = it.first
                    val endDate: Long = it.second
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    //Toast.makeText(this, "${datePicker.headerText} is selected" + "${format.format(startDate).toString()}", Toast.LENGTH_LONG).show()
                    dateInterval1 = LocalDate.parse(format.format(startDate))
                    dateInterval2 = LocalDate.parse(format.format(endDate))
                    dateInterval1AutoCompleteTextView.setText(dateInterval1.format(formatter).toString())
                    dateInterval2AutoCompleteTextView.setText(dateInterval2.format(formatter).toString())
                }
                // Setting up the event for when cancelled is clicked
                datePicker.addOnNegativeButtonClickListener {
                    //Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
                }
                // Setting up the event for when back button is pressed
                datePicker.addOnCancelListener {
                    //Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
                }
            }

            dateInterval2AutoCompleteTextView.setOnClickListener {
                //getCurrentDateTime()
                //DatePickerDialog(this, this, currentDate.year, PlantAction.monthForCalendar(currentDate.monthValue.toInt()), currentDate.dayOfMonth).show()

                val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
                datePicker.show(supportFragmentManager, "DatePicker")

                // Setting up the event for when ok is clicked
                datePicker.addOnPositiveButtonClickListener {
                    val startDate: Long = it.first
                    val endDate: Long = it.second
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    //Toast.makeText(this, "${datePicker.headerText} is selected" + "${format.format(startDate).toString()}", Toast.LENGTH_LONG).show()
                    dateInterval1 = LocalDate.parse(format.format(startDate))
                    dateInterval2 = LocalDate.parse(format.format(endDate))
                    dateInterval1AutoCompleteTextView.setText(dateInterval1.format(formatter).toString())
                    dateInterval2AutoCompleteTextView.setText(dateInterval2.format(formatter).toString())
                }
                // Setting up the event for when cancelled is clicked
                datePicker.addOnNegativeButtonClickListener {
                    //Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
                }
                // Setting up the event for when back button is pressed
                datePicker.addOnCancelListener {
                    //Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
                }
            }
            
            // обрабатываем нажатие кнопок добавления действия и отмены
            addPlantActionBtn.setOnClickListener {
                if (selectedPlant != null) {

                    /*var plantActionToDB: PlantAction = PlantAction.insertToDBHandler(
                        selectedPlantId,
                        LocalDate.now().toString(),
                        plantActionNameACTV.text.toString(),
                        plantActionNoteET.text.toString(),
                        PlantAction.parseDateType(dateTypeAutoCompleteTextView.text.toString()),
                        PlantAction.parseTimeInterval(dateTimeIntervalAutoCompleteTextView.text.toString()),
                        Integer.parseInt(certainDateOfMonthAutoCompleteTextView.text.toString()),
                        certainDateOfYearAutoCompleteTextView.text.toString(),
                        weekDaysString,
                        dateInterval1.toString(),
                        dateInterval2.toString()
                    )*/

                    /*var success = db.insertPlantActionToDB(
                        plantId = plantActionToDB.plantId,
                        creationDate = plantActionToDB.creationDate,
                        name = plantActionNameACTV.text.toString(),
                        note = plantActionNoteET.text.toString(),
                        actionDateType = plantActionToDB.actionDateType,
                        everyTimeInterval = plantActionToDB.everyTimeInterval,
                        certainDateOfMonth = plantActionToDB.certainDateOfMonth,
                        certainDateOfYear = plantActionToDB.certainDateOfYear,
                        weekDaySchedule = plantActionToDB.weekDaysSchedule,
                        certainDate1 = plantActionToDB.certainDate1,
                        certainDate2 = plantActionToDB.certainDate2,
                        color = "#FF0000")*/

                    val plantActionToDB = PlantAction(
                        plantId = selectedPlantId,
                        creationDate = LocalDate.now().toString(),
                        name = plantActionNameACTV.text.toString(),
                        note = plantActionNoteET.text.toString()
                    )
                    when(PlantAction.parseDateType(dateTypeAutoCompleteTextView.text.toString())) {
                        0 -> {
                            plantActionToDB.actionDateType = 0
                            when(PlantAction.parseTimeInterval(dateTimeIntervalAutoCompleteTextView.text.toString())) {
                                0 -> plantActionToDB.everyTimeInterval = 0
                                1 -> {
                                    plantActionToDB.everyTimeInterval = 1
                                    if (certainDateOfMonthAutoCompleteTextView.text.toString().equals(PlantAction.certainDateOfMonthList[0])) {
                                        plantActionToDB.certainDateOfMonth = 0
                                    }
                                    else plantActionToDB.certainDateOfMonth = Integer.parseInt(certainDateOfMonthAutoCompleteTextView.text.toString())

                                }
                                2 -> {
                                    plantActionToDB.everyTimeInterval = 2
                                    plantActionToDB.certainDateOfYear = certainDateOfYearAutoCompleteTextView.text.toString()
                                }
                            }
                        }
                        1 -> {
                            plantActionToDB.actionDateType = 0
                            plantActionToDB.weekDaysSchedule = weekDaysString
                        }
                        2 -> {
                            plantActionToDB.actionDateType = 0
                            plantActionToDB.certainDate1 = dateInterval1.toString()
                            plantActionToDB.certainDate2 = dateInterval2.toString()
                        }
                    }

                    var success = db.insertPlantActionToDB(
                        plantId = plantActionToDB.plantId,
                        creationDate = plantActionToDB.creationDate,
                        name = plantActionToDB.name,
                        note = plantActionToDB.note,
                        actionDateType = plantActionToDB.actionDateType,
                        everyTimeInterval = plantActionToDB.everyTimeInterval,
                        certainDateOfMonth = plantActionToDB.certainDateOfMonth,
                        certainDateOfYear = plantActionToDB.certainDateOfYear,
                        weekDaySchedule = plantActionToDB.weekDaysSchedule,
                        certainDate1 = plantActionToDB.certainDate1,
                        certainDate2 = plantActionToDB.certainDate2,
                        color = "#FF0000")

                    //plantNameTV.text = PlantAction.parseDateType(dateTypeAutoCompleteTextView.text.toString()).toString()
                    plantNameTV.text = plantActionToDB.certainDateOfMonth.toString()

                    if (success.toInt() == -1) {
                        Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show();
                        addPlantActionDialog.dismiss()
                    }
                    else {
                        updatePlantActionRV(db, selectedPlant)
                        addPlantActionDialog.dismiss()
                    }
                }
            }
            cancelPlantBtn.setOnClickListener {
                addPlantActionDialog.dismiss()
            }
        }
    }

    fun updatePlantActionRV(db: DBHelper, selectedPlant: Plant) {
        selectedPlant.plantActions = db.getSelectedPlantActionData(selectedPlantId)
        plantActionAdapter = PlantActionAdapter(this, selectedPlant.plantActions)
        plantActionRV.adapter = plantActionAdapter
    }

    // показываем или скрываем поля выбора даты, дня недели и пр. в зависимости ее типа (см. в PlantAction)
    fun plantActionDateTypeHandler(
        actionTypeString: String,
        dateTimeIntervalAutoCompleteTextViewWrapper: View,
        certainDateOfMonthAutoCompleteTextViewWrapper: View,
        certainDateOfYearAutoCompleteTextViewWrapper: View,
        weekDaysButtons: View,
        dateInterval1AutoCompleteTextViewWrapper: View,
        dateInterval2AutoCompleteTextViewWrapper: View
    ) {
        var dateType: Int = PlantAction.parseDateType(actionTypeString)
        when (dateType) {
            0 -> {
                dateTimeIntervalAutoCompleteTextViewWrapper.visibility = View.VISIBLE
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
                weekDaysButtons.visibility = View.GONE
                dateInterval1AutoCompleteTextViewWrapper.visibility = View.GONE
                dateInterval2AutoCompleteTextViewWrapper.visibility = dateInterval1AutoCompleteTextViewWrapper.visibility
            }
            1 -> {
                dateTimeIntervalAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
                weekDaysButtons.visibility = View.VISIBLE
                dateInterval1AutoCompleteTextViewWrapper.visibility = View.GONE
                dateInterval2AutoCompleteTextViewWrapper.visibility = dateInterval1AutoCompleteTextViewWrapper.visibility
            }
            2 -> {
                dateTimeIntervalAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
                weekDaysButtons.visibility = View.GONE
                dateInterval1AutoCompleteTextViewWrapper.visibility = View.VISIBLE
                dateInterval2AutoCompleteTextViewWrapper.visibility = dateInterval1AutoCompleteTextViewWrapper.visibility
            }
            else -> {
                dateTimeIntervalAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
                weekDaysButtons.visibility = View.GONE
                dateInterval1AutoCompleteTextViewWrapper.visibility = View.GONE
                dateInterval2AutoCompleteTextViewWrapper.visibility = dateInterval1AutoCompleteTextViewWrapper.visibility
            }
        }
    }

    fun plantActionDateIntervalHandler(
        actionIntervalString: String,
        certainDateOfMonthAutoCompleteTextViewWrapper: View,
        certainDateOfYearAutoCompleteTextViewWrapper: View
    ) {
        var dateInterval: Int = PlantAction.parseTimeInterval(actionIntervalString)
        when (dateInterval) {
            0 -> {
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
            }
            1 -> {
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.VISIBLE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
            }
            2 -> {
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.VISIBLE
            }

            else -> {
                certainDateOfMonthAutoCompleteTextViewWrapper.visibility = View.GONE
                certainDateOfYearAutoCompleteTextViewWrapper.visibility = View.GONE
            }
        }
    }

    fun weekDayInvertValue(elementIndex: Int): Int {
        return (1 - elementIndex)
    }

    fun weekDayClick(index: Int, buttonView: MaterialCardView, buttonValue: Int): Pair<MaterialCardView, Int> {
        return Pair(buttonView, weekDayInvertValue(buttonValue))
    }

    fun changeWeekDayValue(position: Int, valueToChange: Int, weekDaysButtonsArray: ArrayList<Pair<MaterialCardView, Int>>): String {
        var arrayInt = PlantAction.parseIntWeekDays(weekDaysString)
        for (n in arrayInt.indices) {
            if (n == position) {
                arrayInt[n] = valueToChange
            }
        }
        weekDaysString = arrayInt.joinToString("")
        return weekDaysString
    }

    // получаем сегодняшнюю дату и день недели
    public fun getCurrentDateTime() {
        currentDate = LocalDate.now()
        formattedDate = currentDate.format(formatter)
        currentDayOfWeek = PlantAction.dayOfWeekToRus(currentDate.dayOfWeek.toString())
    }

    // пока не требуется
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = PlantAction.monthFromCalendar(month)
        savedYear = year
    }
}
