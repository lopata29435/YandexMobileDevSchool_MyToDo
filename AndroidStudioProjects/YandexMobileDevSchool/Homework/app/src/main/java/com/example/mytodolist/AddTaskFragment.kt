package com.example.mytodolist

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.icu.util.Calendar

class AddTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date: TextView = view.findViewById(R.id.dateTextView)

        val backButton: View = view.findViewById(R.id.closeIcon)
        backButton.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }

        val deleteButton: View = view.findViewById(R.id.deleteLayout)
        deleteButton.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }

        val saveButton: TextView = view.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }

        val switch1: androidx.appcompat.widget.SwitchCompat = view.findViewById(R.id.switchData)
        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SetDeadline(date)
            } else {
                date.visibility = View.GONE
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun SetDeadline(date: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(activity as MainActivity,
            { view, selectedYear, selectedMonth, selectedDay ->

                date.text = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)

            }, year, month, day)
        datePickerDialog.show()
        date.visibility = View.VISIBLE
    }
}
