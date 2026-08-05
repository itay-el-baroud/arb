package com.example.callshield

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BlockedListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BlockedNumberAdapter
    private var fullList: List<BlockedNumber> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_blocked_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerBlocked)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BlockedNumberAdapter(mutableListOf()) { number ->
            AppDatabase.getInstance(requireContext()).blockedNumberDao().delete(number)
            loadData()
        }
        recyclerView.adapter = adapter

        view.findViewById<View>(R.id.fabAdd).setOnClickListener {
            showAddDialog()
        }

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val query = s.toString()
                val filtered = fullList.filter {
                    it.phoneNumber.contains(query) || it.nickname.contains(query)
                }
                adapter.updateData(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loadData()
        return view
    }

    private fun loadData() {
        fullList = AppDatabase.getInstance(requireContext()).blockedNumberDao().getAll()
        adapter.updateData(fullList)
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_number, null)

        val etPhone = dialogView.findViewById<EditText>(R.id.etPhoneNumber)
        val etNickname = dialogView.findViewById<EditText>(R.id.etNickname)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)
        val spinnerDuration = dialogView.findViewById<Spinner>(R.id.spinnerDuration)

        spinnerCategory.adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.categories, android.R.layout.simple_spinner_dropdown_item
        )
        spinnerDuration.adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.durations, android.R.layout.simple_spinner_dropdown_item
        )

        AlertDialog.Builder(requireContext())
            .setTitle("إضافة رقم للحظر")
            .setView(dialogView)
            .setPositiveButton("حظر") { _, _ ->
                val phone = etPhone.text.toString().trim()
                if (phone.isEmpty()) return@setPositiveButton

                val duration = spinnerDuration.selectedItem.toString()
                val blockUntil = when (duration) {
                    "ساعة" -> System.currentTimeMillis() + 3600_000L
                    "يوم" -> System.currentTimeMillis() + 86400_000L
                    "أسبوع" -> System.currentTimeMillis() + 604800_000L
                    else -> 0L
                }

                val blocked = BlockedNumber(
                    phoneNumber = phone,
                    nickname = etNickname.text.toString(),
                    category = spinnerCategory.selectedItem.toString(),
                    blockUntil = blockUntil
                )
                AppDatabase.getInstance(requireContext()).blockedNumberDao().insert(blocked)
                loadData()
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
