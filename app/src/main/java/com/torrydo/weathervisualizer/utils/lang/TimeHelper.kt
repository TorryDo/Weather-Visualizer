package com.torrydo.weathervisualizer.utils.lang

import java.util.*
import kotlin.math.round

object TimeHelper {

    fun currentTimeInMillis(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

}

fun Long.countDays(): Int {


    val time = TimeHelper.currentTimeInMillis() - this

    val rs: Double = time.toDouble() / (86_400_000).toDouble()

    return round(rs).toInt()
}