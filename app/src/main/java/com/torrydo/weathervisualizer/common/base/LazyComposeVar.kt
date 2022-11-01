package com.torrydo.weathervisualizer.common.base

import android.content.Context
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import kotlinx.coroutines.CoroutineScope


@Composable
fun WithLazyComposeVar(
    content: @Composable LazyComposeVar.() -> Unit
) {
    val lazyComposeVar = remember { LazyComposeVar() }
    lazyComposeVar.content()
}


class LazyComposeVar {

    lateinit var context: Context
    lateinit var scope: CoroutineScope
    lateinit var onBackPressedDispatcher: OnBackPressedDispatcher

    @Composable
    inline fun <reified T : Any?> initVar(): T {
        return when (T::class) {

            Context::class -> LocalContext.current as T
            CoroutineScope::class -> rememberCoroutineScope() as T
            OnBackPressedDispatcher::class -> LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher as T

            else -> null as T
        }
    }

}