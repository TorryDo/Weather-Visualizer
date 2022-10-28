package com.torrydo.weathervisualizer.common.base

import android.content.Context
import androidx.compose.runtime.Composable

abstract class BaseComposeVar {

    lateinit var context: Context

    @Composable
    abstract fun SetupCompose()

}