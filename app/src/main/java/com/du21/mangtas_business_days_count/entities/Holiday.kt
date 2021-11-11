package com.du21.mangtas_business_days_count.entities

data class Holiday(val month: Int, val day: Int, val type: HolidayType, val typeInfo: String? = null)
enum class HolidayType {
    FIXED,
    WEEKEND,
    DYNAMIC
}

object HolidayTypeInfo {
    const val QUEEN_BIRTHDAY = "QUEEN_BIRTHDAY"
}
