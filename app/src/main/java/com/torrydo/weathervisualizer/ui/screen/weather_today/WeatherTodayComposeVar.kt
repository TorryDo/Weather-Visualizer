package com.torrydo.weathervisualizer.ui.screen.weather_today

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.torrydo.weathervisualizer.common.base.BaseComposeVar
import org.koin.androidx.compose.getViewModel


@Composable
fun WithWeatherTodayUiState(content: @Composable WeatherTodayComposeVar.(WeatherTodayViewModel) -> Unit) {
    val viewModel: WeatherTodayViewModel = getViewModel()
    val composeVar = remember {
        WeatherTodayComposeVar()
    }
    composeVar.SetupCompose()

    LaunchedEffect(Unit){
        viewModel.updateLocation()
    }

    composeVar.content(viewModel)

}

class WeatherTodayComposeVar : BaseComposeVar() {


    @Composable
    override fun SetupCompose() {
        context = LocalContext.current
    }

}