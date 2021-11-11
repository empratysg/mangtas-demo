package com.du21.mangtas_business_days_count

import android.util.Log
import androidx.lifecycle.LiveData
import com.du21.mangtas_business_days_count.entities.*
import com.du21.mangtas_business_days_count.utils.DateCalculateUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DataSource {
    fun getListHoliday(): List<Holiday>
    suspend fun calculateBusinessDay(startDate: Date, endDate: Date): CalculateResult
}

class AppDataSource() : DataSource {
    private var holidayData: List<Holiday>? = null
    override fun getListHoliday(): List<Holiday> {
        return listOf(
            Holiday(2, 5, HolidayType.FIXED),
            Holiday(5, 23, HolidayType.FIXED),
            Holiday(8, 11, HolidayType.FIXED),
            Holiday(10, 7, HolidayType.FIXED),
            Holiday(11, 13, HolidayType.FIXED),

            Holiday(0, 1, HolidayType.WEEKEND),
            Holiday(3, 1, HolidayType.WEEKEND),
            Holiday(6, 4, HolidayType.WEEKEND),
            Holiday(9, 31, HolidayType.WEEKEND),
            Holiday(11, 29, HolidayType.WEEKEND),

            Holiday(0, 0, HolidayType.DYNAMIC, HolidayTypeInfo.QUEEN_BIRTHDAY),
        )
    }

    override suspend fun calculateBusinessDay(
        startDate: Date,
        endDate: Date
    ): CalculateResult {
        val holidays = holidayData ?: getListHoliday().also { holidayData = it }
        return DateCalculateUtils.countBusinessDays(startDate, endDate, holidays)
    }
}