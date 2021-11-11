package com.du21.mangtas_business_days_count.entities

data class CalculateResult(
    val businessDays: Int,
    val weekendDays:Int,
    val fixedHolidays: Int,
    val fixedHolidaysInWeekend: Int,
    val weekendHolidays: Int,
    val dynamicHoliday: Int
)
