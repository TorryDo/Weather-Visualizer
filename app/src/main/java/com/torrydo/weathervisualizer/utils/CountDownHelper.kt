package com.torrydo.weathervisualizer.utils

import kotlinx.coroutines.delay

object CountDownHelper {

    suspend inline fun afterSeconds(
        sec: Int,
        onFinish: () -> Unit
    ) {

        var tick = 0

        while (true) {
            if (tick > sec) break
            delay(1000L)
            tick++
        }

        onFinish()

    }

    suspend fun onEachSecond(sec: Int, listener: (Int) -> Unit) {
        var tick = sec

        while (true) {
            if (tick <= 0) break
            listener.invoke(tick)
            delay(1000L)
            tick--
        }
    }

}