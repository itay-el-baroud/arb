package com.example.callshield

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class LogAdapter(private val items: List<CallLog>) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumber: TextView = view.findViewById(R.id.tvLogNumber)
        val tvTime: TextView = view.findViewById(R.id.tvLogTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val typeLabel = if (item.type == "call") "مكالمة" else "رسالة"
        holder.tvNumber.text = "${item.phoneNumber} • $typeLabel"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvTime.text = sdf.format(Date(item.timestamp))
    }

    override fun getItemCount(): Int = items.size
    }
