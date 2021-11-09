package com.du21.mangtas_business_days_count.common

import android.app.DatePickerDialog
import android.widget.DatePicker

class DefaultDateSetListener(private val callback: (year: Int, month: Int, dayOfMonth: Int) -> Unit) :
    DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        callback.invoke(year, month, dayOfMonth)
    }
}