package com.torrydo.weathervisualizer.ui.screen.next7days

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.torrydo.compose_easier.view.IconEz
import com.torrydo.weathervisualizer.common.base.WithLazyComposeVar
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.ui.assets.IconProvider
import com.torrydo.weathervisualizer.ui.assets.fromDrawable
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun Next7DaysScreen(
) = WithLazyComposeVar {

    onBackPressedDispatcher = initVar()

    val viewModel: Next7DayViewModel = getViewModel()


    viewModel.collectSideEffect {
        when (it) {
            is Next7DaySideEffect.NavigateBack -> onBackPressedDispatcher.onBackPressed()
        }
    }

    // UI ------------------------------------------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        viewModel.TopBarComponent(
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(text = "Today", modifier = Modifier.fillMaxWidth())

            viewModel.WeatherPerHourComponent(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            viewModel.FollowingDaysComponents(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

        }
    }


}

private fun WeatherInfo.getWeatherSummaryFollowingDays(): List<WeatherData> {
    val temp = this.weatherPerDay.minus(0)

    return temp.map { it.value[0] }
}

@Composable
private fun Next7DayViewModel.FollowingDaysComponents(
    modifier: Modifier = Modifier
) {

    val state = collectAsState().value

    LazyColumn(modifier = modifier) {
        items(state.getWeatherSummaryFollowingDays()) { weatherData ->

            Spacer(modifier = Modifier.height(10.dp))

            WeatherItem(weatherData = weatherData)
        }
    }
}

@Composable
private fun WeatherItem(modifier: Modifier = Modifier, weatherData: WeatherData) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(text = "Tomorrow")
            Text(text = "1 Nov")
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(text = weatherData.temperature.toString())

        Spacer(modifier = Modifier.weight(0.3f))

        IconEz.Static(icon = {
            Image(
                modifier = modifier,
                imageVector = fromDrawable(id = weatherData.weatherType.iconRes),
                contentDescription = weatherData.weatherType.weatherDesc
            )
        })
    }
}

@Composable
private fun Next7DayViewModel.WeatherPerHourComponent(
    modifier: Modifier = Modifier
) {

    val state = collectAsState().value

    LazyRow(
        modifier = modifier
    ) {
        items(state.getWeatherCurrentDay()) { item ->
            Column(
                modifier = Modifier
                    .width(70.dp)
                    .height(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${item.time.hour}h", color = Color.Gray, fontWeight = FontWeight.Bold)
                IconEz.Static(
                    icon = {
                        Image(
                            modifier = Modifier.size(40.dp),
                            imageVector = ImageVector.vectorResource(id = item.weatherType.iconRes),
                            contentDescription = "weather type icon",
                        )
                    }, modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                Text(text = "${item.temperature}℃", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Next7DayViewModel.TopBarComponent(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.background(Color.Gray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconEz.Clickable(
            icon = { IconProvider.LeftArrow() },
            onClick = { navigateBack() })

    }
}

