package com.example.myplantsmylove

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        var plantImage: ImageView = itemView.findViewById(R.id.plant_image)

        private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"

        fun bind(plant: Plant) {
            plantName.text = plant.name
            plantNote.text = plant.note
            plantLocation.text = plant.location
            Glide.with(itemView.context)
                .load(R.drawable.plant)
                .centerCrop()
                .into(plantImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflater.inflate(R.layout.plant_cell, parent, false)
        return PlantAdapter.PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {

        holder.bind(plantArrayList[position])

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