package com.torrydo.weathervisualizer.ui.screen.weather_today

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torrydo.weathervisualizer.domain.weather.WeatherType
import com.torrydo.weathervisualizer.ui.assets.IconProvider
import com.torrydo.weathervisualizer.ui.assets.StaticIcon
import com.torrydo.weathervisualizer.ui.theme.BLUE_LIGHT
import com.torrydo.weathervisualizer.ui.theme.BLUE_LIGHTER
import com.torrydo.weathervisualizer.ui.theme.MyColor
import com.torrydo.weathervisualizer.utils.andr.showShortToast
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


//@Preview(showBackground = true)
@Composable
fun WeatherTodayScreen() = WithWeatherTodayUiState { viewModel ->

    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is WeatherTodaySideEffect.Toast -> {
                context.showShortToast("hello")
            }
            is WeatherTodaySideEffect.NavigateToNext7DaysScreen -> {
                context.showShortToast("fake navigating")
            }
        }
    }

    // UI ------------------------------------------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        CardComponent(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .weight(1f),
            viewModel = viewModel,
            state = state
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Today")
                Text(text = "Next 7 Days")
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .weight(1f)
            ) {
                items(state.weatherPerHour) { item ->
                    Column(
                        modifier = Modifier
                            .width(70.dp)
                            .height(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${item.time.hour}h")
                        StaticIcon(icon = {
                            Image(
                                imageVector = ImageVector.vectorResource(id = item.weatherType.iconRes),
                                contentDescription = "weather type icon"
                            )
                        })
                        Text(text = "${item.temperature}℃")
                    }
                }
            }

        }

    }
}

@Composable
private fun WeatherTodayComposeVar.CardComponent(
    modifier: Modifier = Modifier,
    state: WeatherTodayState,
    viewModel: WeatherTodayViewModel
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4),
        backgroundColor = MyColor.BLUE_LIGHT,
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CityItem(cityName = state.locationName)

                Text(text = "Today ${state.time.toInt()}h")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "${state.temperature}℃", fontWeight = FontWeight.Bold, fontSize = 60.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = state.weatherType?.weatherDesc ?: "", fontSize = 20.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(text = "${state.pressure}hpa")
                Text(text = "${state.humidity}%")
                Text(text = "${state.windSpeed}km/h")

            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(5))
                    .background(MyColor.BLUE_LIGHTER)
            ) {

            }

        }
    }
}

@Composable
private fun CityItem(modifier: Modifier = Modifier, cityName: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconProvider.MarkerOutlined()
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = cityName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
