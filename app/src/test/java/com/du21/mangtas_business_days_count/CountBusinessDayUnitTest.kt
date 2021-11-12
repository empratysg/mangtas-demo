package com.du21.mangtas_business_days_count

import com.du21.mangtas_business_days_count.entities.Date
import com.du21.mangtas_business_days_count.entities.Holiday
import com.du21.mangtas_business_days_count.utils.DateCalculateUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CountBusinessDayUnitTest {

    private val dataSource = FakeDataSource()

    @Test
    fun range0Day_isCorrect() {
        assertEquals(
            0,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 1),
                Date(2021, 10, 2),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun rangeLessThan1Week_isCorrect() {
        assertEquals(
            3,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 1),
                Date(2021, 10, 5),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun rangeLessThan1WeekWithSaturday_isCorrect() {
        assertEquals(
            2,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 3),
                Date(2021, 10, 7),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun rangeLessThan1WeekWithSunday_isCorrect() {
        assertEquals(
            2,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 6),
                Date(2021, 10, 10),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun rangeLessThan1WeekWithWeekend_isCorrect() {
        assertEquals(
            2,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 5),
                Date(2021, 10, 10),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range1Week_isCorrect() {
        assertEquals(
            5,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 1),
                Date(2021, 10, 9),
                dataSource.getListHoliday()
            ).businessDays
        )
    }
    @Test
    fun range1WeekStartSunday_isCorrect() {
        // 1 day weekend holiday
        assertEquals(
            4,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 9, 30),
                Date(2021, 10, 7),
                dataSource.getListHoliday()
            ).businessDays
        )
    }


    @Test
    fun range1WeekStartSaturday_isCorrect() {
        // 1 day fixed holiday in weekend
        assertEquals(
            5,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 5),
                Date(2021, 10, 13),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range1Month_isCorrect() {
        // case start with 1st and last day of month
        // 1 day fixed holiday in weekend
        assertEquals(
            22,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 9, 31),
                Date(2021, 11, 1),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range30daysStartSunday_isCorrect() {
        // 1 day fixed holiday in weekend
        // 1 day weekend holiday
        assertEquals(
            20,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 9, 23),
                Date(2021, 10, 23),
                dataSource.getListHoliday()
            ).businessDays
        )
    }


    @Test
    fun range30DaysStartSaturday_isCorrect() {
        // 1 day fixed holiday in weekend
        assertEquals(
            20,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 12),
                Date(2021, 11, 13),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range30Days_isCorrect() {
        // 1 day fixed holiday in work day
        assertEquals(
            21,
            DateCalculateUtils.countBusinessDays(
                Date(2021, 10, 15),
                Date(2021, 11, 16),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range1Year_isCorrect() {
        assertEquals(
            252,
            DateCalculateUtils.countBusinessDays(
                Date(2020, 11, 31),
                Date(2022, 0, 1),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range1YearBetween2Years_isCorrect() {
        assertEquals(
            252,
            DateCalculateUtils.countBusinessDays(
                Date(2020, 6, 16),
                Date(2021, 6, 17),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range1YearBetween2Years2_isCorrect() {
        assertEquals(
            347,
            DateCalculateUtils.countBusinessDays(
                Date(2020, 2, 21),
                Date(2021, 7, 9),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range2Year_isCorrect() {
        assertEquals(
            503,
            DateCalculateUtils.countBusinessDays(
                Date(2019, 6, 16),
                Date(2021, 6, 17),
                dataSource.getListHoliday()
            ).businessDays
        )
    }

    @Test
    fun range3Year_isCorrect() {

        assertEquals(
            928,
            DateCalculateUtils.countBusinessDays(
                Date(2019, 2, 5),
                Date(2022, 10, 10),
                dataSource.getListHoliday()
            ).businessDays
        )
    }
}
