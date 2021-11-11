package com.du21.mangtas_business_days_count

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.du21.mangtas_business_days_count.common.DateValidator
import com.du21.mangtas_business_days_count.entities.CalculateResult
import com.du21.mangtas_business_days_count.entities.Date
import com.du21.mangtas_business_days_count.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val dataSource: DataSource,
    private val dateValidator: DateValidator
) : ViewModel() {

    val startDateStr = MutableLiveData<String>()
    val endDateStr = MutableLiveData<String>()
    val calculateResult = MutableLiveData<CalculateResult>()
    val inputStartError = MutableLiveData<Int>()
    val inputEndError = MutableLiveData<Int>()

    fun setStartDate(year: Int, month: Int, day: Int) {
        startDateStr.value =
            "$year/${String.format("%02d", month + 1)}/${String.format("%02d", day)}"
    }

    fun setEndDate(year: Int, month: Int, day: Int) {
        endDateStr.value = "$year/${String.format("%02d", month + 1)}/${String.format("%02d", day)}"
    }

    fun onStartDateTextChange(s: String) {
        with(startDateStr) {
            value = s
            inputStartError.value = if (dateValidator.isValid(value)) {
                -1
            } else {
                R.string.error_start_date_invalid
            }
        }


    }

    fun onEndDateTextChange(s: String) {
        with(endDateStr) {
            value = s
            inputEndError.value = if (dateValidator.isValid(value)) {
                -1
            } else {
                R.string.error_end_date_invalid
            }
        }

    }

    fun calculate() {
        val startText = startDateStr.value
        val endText = endDateStr.value
        if (!dateValidator.isValid(startText) || !dateValidator.isValid(endText)) {
            return
        }
        if (!checkValidDateRange(startText, endText)) {
            inputStartError.value = R.string.error_start_date_before_end_date
        }
        viewModelScope.launch(Dispatchers.IO) {
            val startDate = getDateFromString(startText)
            val endDate = getDateFromString(endText)
            if (startDate != null && endDate != null) {
                val result = dataSource.calculateBusinessDay(startDate, endDate)
                calculateResult.postValue(result)
            }
        }
    }

    private fun checkValidDateRange(startDateText: String?, endDateText: String?): Boolean {
        if (startDateText == null || endDateText == null) return false

        val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)

        try {

            val startDate = dateFormat.parse(startDateText)
            val endDate = dateFormat.parse(endDateText)
            return startDate!!.time < endDate!!.time

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    private fun getDateFromString(dateStr: String?): Date? {
        dateStr ?: return null
        val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)
        try {
            return dateFormat.parse(dateStr)?.run {
                val c = Calendar.getInstance().apply { time = this@run }
                Date(c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH])
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}