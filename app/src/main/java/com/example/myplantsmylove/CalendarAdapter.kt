package com.example.myplantsmylove

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
        var context: Context,
        var daysInMonth: ArrayList<String>
    ): RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var dayOfMonth: TextView = itemView.findViewById(R.id.calendar_cell_day_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        var layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height.toInt() * 0.166666666).toInt()
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysInMonth.get(position).toString()
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }
}