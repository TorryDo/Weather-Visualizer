package com.torrydo.weathervisualizer.utils.andr

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
fun isHigherThanAndroid8() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
//fun Float.toPx(): Int = (this * getSystem().displayMetrics.density).toPx()

inline val Context.widthPx: Int get() = resources.displayMetrics.widthPixels
inline val Context.heightPx: Int get() = resources.displayMetrics.heightPixels

inline val Context.widthDp: Int get() = widthPx.toDp()
inline val Context.heightDp: Int get() = heightPx.toDp()

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}