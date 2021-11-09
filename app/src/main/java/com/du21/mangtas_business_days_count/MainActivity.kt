package com.du21.mangtas_business_days_count

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.widget.DatePicker
import com.du21.mangtas_business_days_count.common.DefaultDateSetListener
import com.du21.mangtas_business_days_count.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var startDate: Date? = null
    private var endDate: Date? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickStartDate.setOnClickListener {
            openDatePicker(startDate) { year, month, dayOfMonth ->
                startDate = startDate?.apply {
                    this.year = year
                    this.month = month
                    this.day = dayOfMonth
                } ?: Date(year, month, dayOfMonth)
                binding.edtStartDate.setText(dateToString(startDate))
            }
        }
        binding.btnPickEndDate.setOnClickListener {
            openDatePicker(endDate) { year, month, dayOfMonth ->
                endDate = endDate?.apply {
                    this.year = year
                    this.month = month
                    this.day = dayOfMonth
                } ?: Date(year, month, dayOfMonth)
                binding.edtEndDate.setText(dateToString(endDate))
            }

        }
        binding.btnCalculate.setOnClickListener {
            if (startDate != null && endDate != null) {
                calculateDateInRange(startDate!!, endDate!!)
            }
        }
    }

    private fun openDatePicker(
        currentPick: Date?,
        callback: (year: Int, month: Int, dayOfMonth: Int) -> Unit
    ) {
        val y: Int
        val m: Int
        val d: Int
        if (currentPick == null) {
            val calendar = Calendar.getInstance()
            y = calendar[Calendar.YEAR]
            m = calendar[Calendar.MONTH]
            d = calendar[Calendar.DAY_OF_MONTH]
        } else {
            y = currentPick.year
            m = currentPick.month
            d = currentPick.day
        }
        val datePicker = DatePickerDialog(this, DefaultDateSetListener(callback), y, m, d)
        datePicker.show()
    }

    private fun dateToString(d: Date?): String {
        return d?.run { "$year/${month + 1}/$day" } ?: ""
    }

    private fun calculateDateInRange(start: Date, end: Date) {
        val cStart = Calendar.getInstance().apply {
            set(start.year, start.month, start.day, 0, 0, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val cEnd = Calendar.getInstance().apply {
            set(end.year, end.month, end.day, 0, 0, 0)
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val t = cEnd.timeInMillis - cStart.timeInMillis
        val dayDif = t / (1000 * 60 * 60 * 24) + 1

        val dayOfWeekStart = cStart.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekEnd = cEnd.get(Calendar.DAY_OF_WEEK)

        val weeks = (dayDif - (7-dayOfWeekStart+1) - (dayOfWeekEnd)) / 7
        var weekkends = weeks * 2
            weekkends += if (dayOfWeekStart != 1) 1 else 0
            weekkends += if (dayOfWeekEnd != 7) 1 else 0
        Log.e("calculateDateInRange", "numberWeekends: " + weekkends)
        Log.e("calculateDateInRange", "calculateDateInRange: " + dayDif)
    }
}