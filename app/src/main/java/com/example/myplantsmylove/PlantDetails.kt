package com.example.myplantsmylove

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplantsmylove.SQLiteDBClasses.DBHelper
import java.time.LocalDate

class PlantDetails : AppCompatActivity() {

    lateinit var plantIdTV: TextView
    lateinit var plantNameTV: TextView
    lateinit var plantNoteTV: TextView

    lateinit var plantActionDialogBtn: Button

    lateinit var plantActionRV: RecyclerView
    lateinit var plantActionAdapter: PlantActionAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager


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

            var addPlantActionBtn: Button = dialogView.findViewById(R.id.add_plant_action_btn)
            var cancelPlantBtn: Button = dialogView.findViewById(R.id.cancel_plant_action_btn)
            var plantNameET: EditText = dialogView.findViewById(R.id.plant_action_name_et)
            var plantNoteET: EditText = dialogView.findViewById(R.id.plant_action_note_et)
            var plantNoteETWrapper: EditText = dialogView.findViewById(R.id.plant_action_note_et_wrapper)

            // инициализируем и обрабатываем выпадающее меню
            var dateTypeArrayAdapter = ArrayAdapter(this, R.layout.add_action_dropdown_menu_list_item, PlantAction.dateTypeList)
            var dateTypeAutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.action_date_type_auto_complete_tv)
            dateTypeAutoCompleteTextView.setAdapter(dateTypeArrayAdapter)

            var dateTimeIntervalArrayAdapter = ArrayAdapter(this, R.layout.add_action_dropdown_menu_list_item, PlantAction.timeIntervalList)
            var dateTimeIntervalAutoCompleteTextView: AutoCompleteTextView = dialogView.findViewById(R.id.date_time_interval_auto_complete_tv)
            dateTimeIntervalAutoCompleteTextView.setAdapter(dateTimeIntervalArrayAdapter)
            
            dateTypeAutoCompleteTextView.setOnItemClickListener { parent, view, position, id -> plantActionDateTypeHandler(dateTypeAutoCompleteTextView.text.toString(), plantNoteET, plantNoteETWrapper) }
            dateTimeIntervalAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->  }
            
            // обрабатываем нажатие кнопок добавления действия и отмены
            addPlantActionBtn.setOnClickListener {
                if (selectedPlant != null) {
                    var success = db.insertPlantActionToDB(plantId = selectedPlantId, creationDate = LocalDate.now().toString(), name = "Растение ${selectedPlantId}", note = "Заметочка растения $selectedPlantId", actionDateType = 1, everyTimeInterval = 1, certainDateOfMonth = -1, certainDateOfYear = "", weekDaySchedule = "!0000000", certainDate1 = "", certainDate2 = "", color = "#FF0000")

                    plantNameTV.text = PlantAction.parseDateType(dateTypeAutoCompleteTextView.text.toString()).toString()
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
    fun plantActionDateTypeHandler(actionTypeString: String, testView: View, testViewWrapper: View) {
        var dateType: Int = PlantAction.parseDateType(actionTypeString)
        when (dateType) {
            0 -> {
                testViewWrapper.visibility = View.GONE
            }
            1 -> {
                testViewWrapper.visibility = View.VISIBLE

            }
            2 -> {
                plantNameTV.text = "2"
            }

            else -> plantNameTV.text = "Ошибка типа действия"
        }
    }


}
