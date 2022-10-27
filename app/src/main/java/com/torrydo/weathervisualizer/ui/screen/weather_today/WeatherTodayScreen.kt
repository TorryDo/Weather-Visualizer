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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torrydo.weathervisualizer.domain.weather.WeatherType
import com.torrydo.weathervisualizer.ui.assets.IconProvider
import com.torrydo.weathervisualizer.ui.assets.StaticIcon
import com.torrydo.weathervisualizer.ui.theme.BLUE_LIGHT
import com.torrydo.weathervisualizer.ui.theme.BLUE_LIGHTER
import com.torrydo.weathervisualizer.ui.theme.MyColor

@Preview(showBackground = true)
@Composable
fun WeatherTodayScreen() {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        CardComponent(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .weight(1f)
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
                items(fakeWeather) { item ->
                    Column(
                        modifier = Modifier
                            .width(70.dp)
                            .height(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${item.hour}h")
                        StaticIcon(icon = {
                            Image(
                                imageVector = ImageVector.vectorResource(id = item.weatherType.iconRes),
                                contentDescription = "weather type icon"
                            )
                        })
                        Text(text = "${item.celsius}^c")
                    }
                }
            }

        }

    }
}

private data class WeatherInfo(
    val hour: Int,
    val weatherType: WeatherType,
    val celsius: Float
)

private val fakeWeather = listOf(
    WeatherInfo(0, WeatherType.ClearSky, 22f),
    WeatherInfo(1, WeatherType.DenseDrizzle, 21.4f),
    WeatherInfo(2, WeatherType.DenseFreezingDrizzle, 24f),
    WeatherInfo(3, WeatherType.ModerateSnowFall, 23f),
    WeatherInfo(4, WeatherType.DepositingRimeFog, 25f),
    WeatherInfo(5, WeatherType.Foggy, 27f),
    WeatherInfo(6, WeatherType.LightFreezingDrizzle, 30f),
)

@Composable
private fun CardComponent(modifier: Modifier = Modifier) {
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
                CityItem(cityName = "Gotham")
                TimeItem()
            }

            CelsiusComponent()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(text = "720hpa")
                Text(text = "32%")
                Text(text = "12km/h")

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
private fun CelsiusComponent(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "24'C", fontWeight = FontWeight.Bold, fontSize = 60.sp)
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Mostly Clear", fontSize = 20.sp)
}

@Composable
private fun CityItem(modifier: Modifier = Modifier, cityName: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconProvider.MarkerOutlined()
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = cityName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TimeItem(modifier: Modifier = Modifier) {
    val data = "Today 00:32 PM"
    Text(text = data)
}
