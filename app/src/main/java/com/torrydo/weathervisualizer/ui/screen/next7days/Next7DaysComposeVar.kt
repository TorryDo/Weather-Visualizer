package com.torrydo.weathervisualizer.ui.screen.next7days

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.torrydo.weathervisualizer.common.base.BaseComposeVar
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.ui.screen.current_weather.WeatherTodayState
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun WithNext7DaysComposeVar(content: @Composable Next7DaysComposeVar.() -> Unit) {
    val composeVar = remember {
        Next7DaysComposeVar()
    }
    composeVar.SetupCompose()

    composeVar.content()

}

class Next7DaysComposeVar : BaseComposeVar<WeatherInfo, Next7DaysViewModel>() {



    @Composable
    override fun SetupCompose() {
        context = LocalContext.current
        scope = rememberCoroutineScope()
        onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        _viewModel = getViewModel()
        _state = _viewModel.collectAsState()

        LaunchedEffect(Unit){
            viewModel.updateWeatherInfo()
        }

    }

}