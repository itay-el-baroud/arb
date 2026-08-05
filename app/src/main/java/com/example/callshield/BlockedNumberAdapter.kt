package com.example.callshield

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BlockedNumberAdapter(
    private var items: MutableList<BlockedNumber>,
    private val onDelete: (BlockedNumber) -> Unit
) : RecyclerView.Adapter<BlockedNumberAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNickname: TextView = view.findViewById(R.id.tvNickname)
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)
        val tvAttempts: TextView = view.findViewById(R.id.tvAttempts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blocked_number, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNickname.text = if (item.nickname.isNotEmpty()) item.nickname else item.phoneNumber
        holder.tvNumber.text = "${item.phoneNumber} • ${item.category}"
        holder.tvAttempts.text = "عدد المحاولات: ${item.attemptCount}"
        holder.itemView.setOnLongClickListener {
            onDelete(item)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<BlockedNumber>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
