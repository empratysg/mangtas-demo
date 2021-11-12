package com.du21.mangtas_business_days_count.utils

import com.du21.mangtas_business_days_count.entities.*
import com.du21.mangtas_business_days_count.entities.Date
import java.util.*
import kotlin.collections.HashMap

object DateCalculateUtils {
    private const val ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24

    fun countBusinessDays(
        startDate: Date,
        endDate: Date,
        holidaysArray: List<Holiday>
    ): CalculateResult {
        val cStart = Calendar.getInstance().apply {
            set(startDate.year, startDate.month, startDate.day, 0, 0, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val cEnd = Calendar.getInstance().apply {
            set(endDate.year, endDate.month, endDate.day, 0, 0, 0)
            add(Calendar.DAY_OF_YEAR, -1)
        }

        if (cEnd.before(cStart)) {
            return CalculateResult(
                0,
                0,
                0,
                0,
                0,
                0
            )
        }

        val t = cEnd.timeInMillis - cStart.timeInMillis
        val dayDif = t / (ONE_DAY_IN_MILLIS) + 1

        val weekends = countWeekends(cStart, cEnd)

        val holidayMap = HashMap<HolidayType, MutableList<Holiday>>()
        holidaysArray.forEach { h ->
            if (holidayMap.containsKey(h.type)) {
                holidayMap[h.type]?.add(h)
            } else {
                holidayMap[h.type] = mutableListOf<Holiday>().apply { add(h) }
            }
        }
        val fixedHolidays = countFixedHolidays(cStart, cEnd, holidayMap[HolidayType.FIXED])
        val weekendHolidays = countWeekendHolidays(cStart, cEnd, holidayMap[HolidayType.WEEKEND])
        val dynamicHolidays = countDynamicHolidays(cStart, cEnd, holidayMap[HolidayType.DYNAMIC])
        val businessDays =
            (dayDif - weekends - fixedHolidays.first - weekendHolidays - dynamicHolidays).toInt()
        println("businessDays:${businessDays}")
        println("Weekends:${weekends}")
        println("fixedHolidays:${fixedHolidays.first}-fixedHolidays(in weekend):${fixedHolidays.second}")
        println("weekendHolidays:$weekendHolidays")
        println("dynamicHolidays:$dynamicHolidays")

        return CalculateResult(
            businessDays,
            weekends,
            fixedHolidays.first,
            fixedHolidays.second,
            weekendHolidays,
            dynamicHolidays
        )
    }

    private fun countWeekends(cStart: Calendar, cEnd: Calendar): Int {
        if (cEnd.before(cStart)) {
            return 0
        }

        val dayOfWeekStart = cStart[Calendar.DAY_OF_WEEK]
        val dayOfWeekEnd = cEnd[Calendar.DAY_OF_WEEK]

        val t = cEnd.timeInMillis - cStart.timeInMillis
        val dayDif = t / (ONE_DAY_IN_MILLIS) + 1

        val weeks = (dayDif - (7 - dayOfWeekStart + 1) - (dayOfWeekEnd)) / 7
        var weekends: Long = weeks * 2
        weekends += if (dayOfWeekStart != 1) 1 else 2
        weekends += if (dayOfWeekEnd != 7) 1 else 2
        return weekends.toInt()
    }

    private fun countFixedHolidays(
        cStart: Calendar,
        cEnd: Calendar,
        holidaysArray: List<Holiday>?
    ): Pair<Int, Int> {
        if (cEnd.before(cStart) || holidaysArray == null) {
            return Pair(0, 0)
        }
        val startYear = cStart[Calendar.YEAR]
        val endYear = cEnd[Calendar.YEAR]
        val yearCount = endYear - startYear + 1

        var count = 0
        var countInWeekend = 0
        val c = Calendar.getInstance()
        for (i in 0 until yearCount) {
            for (holiday in holidaysArray) {
                c.set(startYear + i, holiday.month, holiday.day, 0, 0, 0)
                if (c.timeInMillis !in cStart.timeInMillis..cEnd.timeInMillis) {
                    continue
                }
                val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
                if ((dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY)) {
                    count++
                } else {
                    countInWeekend++
                }

            }
        }
        return Pair(count, countInWeekend)
    }

    private fun countWeekendHolidays(
        cStart: Calendar,
        cEnd: Calendar,
        holidaysArray: List<Holiday>?
    ): Int {
        if (cEnd.before(cStart) || holidaysArray == null) {
            return 0
        }
        val startYear = cStart[Calendar.YEAR]
        val endYear = cEnd[Calendar.YEAR]
        val yearCount = endYear - startYear + 1


        var count = 0
        val c = Calendar.getInstance()
        if (yearCount == 1) {

            holidaysArray.forEach { h ->
                c.set(startYear, h.month, h.day, 0, 0, 0)
                if(checkRangeWeekendHoliday(c,cStart, cEnd)){
                    count++
                }
            }
        } else {
            val lastDayOfStartCalendar = Calendar.getInstance().apply {
                set(startYear + 1, 0, 1, 0, 0, 0)
                add(Calendar.DAY_OF_YEAR, -1)
            }
            val firstDayOfEndCalendar = Calendar.getInstance().apply {
                set(endYear, 0, 1, 0, 0, 0)
            }
            count += (yearCount - 2) * holidaysArray.size

            holidaysArray.forEach { h ->
                c.set(startYear, h.month, h.day, 0, 0, 0)

                if (checkRangeWeekendHoliday(c,cStart, lastDayOfStartCalendar)) {
                    count++
                }
                c.set(endYear, h.month, h.day, 0, 0, 0)
                if (checkRangeWeekendHoliday(c,firstDayOfEndCalendar, cEnd)) {
                    count++
                }

            }

        }

        return count
    }

    private fun checkRangeWeekendHoliday(c: Calendar, cStart: Calendar, cEnd: Calendar): Boolean {
        if (c.timeInMillis !in cStart.timeInMillis..cEnd.timeInMillis) {
            return false
        }
        if (c[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY &&
            (c[Calendar.DAY_OF_YEAR] == cEnd[Calendar.DAY_OF_YEAR] ||
                    c[Calendar.DAY_OF_YEAR] == cEnd[Calendar.DAY_OF_YEAR] - 1)
        ) {

            return false

        }
        if (c[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY && c[Calendar.DAY_OF_YEAR] == cEnd[Calendar.DAY_OF_YEAR]) {
            return false
        }
        return true
    }

    private fun countDynamicHolidays(
        cStart: Calendar,
        cEnd: Calendar,
        holidaysArray: List<Holiday>?
    ): Int {
        if (cEnd.before(cStart) || holidaysArray == null) {
            return 0
        }
        val startYear = cStart[Calendar.YEAR]
        val endYear = cEnd[Calendar.YEAR]

        val yearCount = endYear - startYear + 1

        var count = 0
        if (yearCount == 1) {

            holidaysArray.forEach { h ->
                when (h.typeInfo) {
                    HolidayTypeInfo.QUEEN_BIRTHDAY -> {
                        if (checkQueenBirthDay(cStart, cEnd, startYear)) {
                            count++
                        }
                    }
                }
            }
        } else {
            val lastDayOfStartCalendar = Calendar.getInstance().apply {
                set(startYear + 1, 0, 1, 0, 0, 0)
                add(Calendar.DAY_OF_YEAR, -1)
            }
            val firstDayOfEndCalendar = Calendar.getInstance().apply {
                set(endYear, 0, 1, 0, 0, 0)
            }
            count += (yearCount - 2) * holidaysArray.size

            holidaysArray.forEach { h ->
                when (h.typeInfo) {
                    HolidayTypeInfo.QUEEN_BIRTHDAY -> {
                        if (checkQueenBirthDay(cStart, lastDayOfStartCalendar, startYear)) {
                            count++
                        }
                        if (checkQueenBirthDay(firstDayOfEndCalendar, cEnd, endYear)) {
                            count++
                        }
                    }
                }
            }
        }

        return count
    }

    private fun checkQueenBirthDay(
        cStart: Calendar,
        cEnd: Calendar,
        year2Check: Int
    ): Boolean {
        val c = Calendar.getInstance().apply {
            set(year2Check, 5, 1, 0, 0, 0)
        }
        val dayOfWeek1st = c[Calendar.DAY_OF_WEEK]
        val firstWeekend = 7 - dayOfWeek1st + 1
        val secondMonday = if (dayOfWeek1st <= 2) {
            firstWeekend + 2
        } else {
            firstWeekend + 7 + 2

        }
        c.set(Calendar.DAY_OF_MONTH, secondMonday)

        return c.timeInMillis in cStart.timeInMillis..cEnd.timeInMillis
    }

}