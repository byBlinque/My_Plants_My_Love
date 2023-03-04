package com.example.myplantsmylove
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        var plantActionImage: ImageView = itemView.findViewById(R.id.plant_action_image)

        fun bind(plantAction: PlantAction)  {
            plantActionName.text = plantAction.name
            plantActionNote.text = plantAction.note
            plantActionDate.text = PlantAction.plantActionDateText(plantAction)
            when(plantAction.name) {
                PlantAction.actionNameList[0] -> Glide.with(itemView.context).load(R.drawable.plant_watering).centerCrop().into(plantActionImage)
                PlantAction.actionNameList[1] -> Glide.with(itemView.context).load(R.drawable.plant_transfer).centerCrop().into(plantActionImage)
                PlantAction.actionNameList[2] -> Glide.with(itemView.context).load(R.drawable.plant_fertilize).centerCrop().into(plantActionImage)
                PlantAction.actionNameList[3] -> Glide.with(itemView.context).load(R.drawable.plant_processing).centerCrop().into(plantActionImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantActionViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflater.inflate(R.layout.plant_action_cell, parent, false)
        return PlantActionAdapter.PlantActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantActionViewHolder, position: Int) {
        holder.bind(plantActionArrayList[position])
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
