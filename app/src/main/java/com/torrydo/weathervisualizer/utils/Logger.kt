package com.torrydo.weathervisualizer.utils

import android.util.Log

fun String?.addLoggerPrefix() = "<> $this"

object Logger {

    val IS_LOGGER_ENABLED = true

    fun d(message: String, tag: String? = javaClass.simpleName.toString()) {
        if (IS_LOGGER_ENABLED) {
            Log.d(tag.addLoggerPrefix(), message)
        }
    }

    fun e(message: String, tag: String? = javaClass.simpleName.toString()) {
        if (IS_LOGGER_ENABLED) {
            Log.e(tag.addLoggerPrefix(), message)
        }
    }

    fun i(message: String, tag: String? = javaClass.simpleName.toString()) {
        if (IS_LOGGER_ENABLED) {
            Log.i(tag.addLoggerPrefix(), message)
        }
    }
}