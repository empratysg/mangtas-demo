package com.du21.mangtas_business_days_count.entities

data class Date(var year: Int, var month: Int, var day: Int)

fun Date.toDateString():String="$year/${month + 1}/$day"