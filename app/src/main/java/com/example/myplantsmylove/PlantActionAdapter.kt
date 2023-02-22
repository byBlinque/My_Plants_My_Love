package com.example.myplantsmylove
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myplantsmylove.SQLiteDBClasses.DBHelper

class PlantActionAdapter(
    var context: Context,
    var plantActionArrayList: ArrayList<PlantAction>
): RecyclerView.Adapter<PlantActionAdapter.PlantActionViewHolder>() {
    class PlantActionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var plantActionName: TextView = itemView.findViewById(R.id.plant_action_name)
        var plantActionNote: TextView = itemView.findViewById(R.id.plant_action_note)
        var plantActionDate: TextView = itemView.findViewById(R.id.plant_action_date)
        var plantActionDelete: TextView = itemView.findViewById(R.id.plant_action_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantActionViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflater.inflate(R.layout.plant_action_cell, parent, false)
        return PlantActionAdapter.PlantActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantActionViewHolder, position: Int) {
        holder.plantActionName.text = plantActionArrayList.get(position).name
        holder.plantActionNote.text = plantActionArrayList.get(position).note
        holder.plantActionDate.text = PlantAction.plantActionDateText(plantActionArrayList.get(position))
        if (holder.plantActionNote.text.equals("")) {
            holder.plantActionNote.text = "Без заметки"
        }

        holder.plantActionDelete.setOnClickListener {
            val db = DBHelper(context)
            var success = db.deletePlantAction(plantActionArrayList.get(position).id)
            if (success == false) {
                holder.plantActionName.text = "Ошибка удаления"
            }
            else {
                plantActionArrayList.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return plantActionArrayList.size
    }
}
