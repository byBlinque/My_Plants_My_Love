package com.example.myplantsmylove

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myplantsmylove.SQLiteDBClasses.DBHelper
import java.util.ArrayList

class PlantAdapter(
    var context: Context,
    var plantArrayList: ArrayList<Plant>
): RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    class PlantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var plantName: TextView = itemView.findViewById(R.id.plant_name)
        var plantNote: TextView = itemView.findViewById(R.id.plant_note)
        var plantLocation: TextView = itemView.findViewById(R.id.plant_location)
        var plantDelete: TextView = itemView.findViewById(R.id.plant_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflater.inflate(R.layout.plant_cell, parent, false)
        return PlantAdapter.PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.plantName.text = plantArrayList.get(position).name
        holder.plantNote.text = plantArrayList.get(position).note
        holder.plantLocation.text = plantArrayList.get(position).location

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlantDetails::class.java)
            intent.putExtra("selectedPlantId", plantArrayList.get(position).id)
            intent.putExtra("selectedPlantPosition", position)
            context.startActivity(intent)
        }
        holder.plantDelete.setOnClickListener {
            val db = DBHelper(context)
            var success = db.deletePlant(plantArrayList.get(position).id)
            if (success == false) {
                holder.plantName.text = "Ошибка удаления"
            }
            else {
                plantArrayList.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return plantArrayList.size
    }



}