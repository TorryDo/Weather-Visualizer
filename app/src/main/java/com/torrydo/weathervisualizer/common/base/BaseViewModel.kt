package com.torrydo.weathervisualizer.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    inline fun ioScope(crossinline block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO){
            block()
        }
    }

}