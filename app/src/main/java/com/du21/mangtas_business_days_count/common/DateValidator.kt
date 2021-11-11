package com.du21.mangtas_business_days_count.common

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

interface DateValidator {
    fun isValid(dateStr: String?): Boolean
}

class DateValidatorUsingDateFormat(private val dateFormat: String) : DateValidator {
    override fun isValid(dateStr: String?): Boolean {
        dateStr ?: return false
        val sdf: DateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        sdf.isLenient = false
        try {
            sdf.parse(dateStr)
        } catch (e: ParseException) {
            return false
        }
        return true
    }
}