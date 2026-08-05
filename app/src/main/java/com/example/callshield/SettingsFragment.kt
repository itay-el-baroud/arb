package com.example.callshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val prefs = requireContext().getSharedPreferences("callshield_prefs", 0)
        val etThreshold = view.findViewById<EditText>(R.id.etSmartBlockThreshold)
        val switchOnlyUnknown = view.findViewById<Switch>(R.id.switchOnlyUnknown)

        etThreshold.setText(prefs.getInt("smart_block_threshold", 0).toString())
        switchOnlyUnknown.isChecked = prefs.getBoolean("only_unknown", false)

        view.findViewById<View>(R.id.btnSaveSettings).setOnClickListener {
            val threshold = etThreshold.text.toString().toIntOrNull() ?: 0
            prefs.edit()
                .putInt("smart_block_threshold", threshold)
                .putBoolean("only_unknown", switchOnlyUnknown.isChecked)
                .apply()
            Toast.makeText(requireContext(), "تم الحفظ", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
