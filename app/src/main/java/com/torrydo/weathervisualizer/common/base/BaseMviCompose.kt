package com.torrydo.weathervisualizer.common.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

@Deprecated("bin")
class MviComposeContainer<STATE, SIDE_EFFECT>(
    private val scope: CoroutineScope,
    initialState: STATE
) {
    private val _stateFlow = MutableStateFlow(initialState)
    val state get() = _stateFlow.asStateFlow()

    private val _sideEffect = Channel<SIDE_EFFECT>(Channel.BUFFERED)
    val sideEffect get() = _sideEffect.receiveAsFlow()

    fun intent(transform: suspend MviComposeContainer<STATE, SIDE_EFFECT>.() -> Unit) {
        scope.launch(SINGLE_THREAD) {
            this@MviComposeContainer.transform()
        }
    }

    suspend fun reduce(reducer: STATE.() -> STATE) {
        withContext(SINGLE_THREAD) {
            _stateFlow.update(reducer)
        }
    }

    suspend fun postSideEffect(effect: SIDE_EFFECT) {
        _sideEffect.send(effect)
    }


    companion object {
        private val SINGLE_THREAD = newSingleThreadContext("base_mvi")
    }
}
