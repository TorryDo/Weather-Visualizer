package com.torrydo.weathervisualizer.ui.screen.current_weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.torrydo.weathervisualizer.common.base.BaseComposeVar
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState


@Composable
fun WithWeatherTodayUiState(content: @Composable WeatherTodayComposeVar.() -> Unit) {
    val composeVar = remember {
        WeatherTodayComposeVar()
    }
    composeVar.SetupCompose()

    composeVar.content()

}

class WeatherTodayComposeVar : BaseComposeVar<WeatherTodayState, WeatherTodayViewModel>() {

    @Composable
    override fun SetupCompose() {
        context = LocalContext.current
        scope = rememberCoroutineScope()

        _viewModel = getViewModel()
        _state = viewModel.collectAsState()


        LaunchedEffect(Unit) {
            viewModel.updateLocation()
        }
    }

}