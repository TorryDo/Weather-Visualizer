package com.torrydo.weathervisualizer.common.base

import android.content.Context
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

abstract class BaseComposeVar<STATE : Any, VIEW_MODEL : Any> {

    protected lateinit var _state: State<STATE>
    val state get() = _state.value

    protected lateinit var _viewModel: VIEW_MODEL
    val viewModel get() = _viewModel

    lateinit var context: Context
    lateinit var scope: CoroutineScope
    var onBackPressedDispatcher: OnBackPressedDispatcher? = null

    @Composable
    abstract fun SetupCompose()

}