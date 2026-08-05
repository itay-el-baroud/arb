package com.example.callshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LogsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerLogs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val logs = AppDatabase.getInstance(requireContext()).callLogDao().getAll()
        recyclerView.adapter = LogAdapter(logs)

        return view
    }
}
