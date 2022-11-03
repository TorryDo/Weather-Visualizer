package com.torrydo.weathervisualizer.ui.screen.current_weather

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.torrydo.compose_easier.ext.noRippleClickable
import com.torrydo.compose_easier.navigation.ext.to
import com.torrydo.compose_easier.view.IconEz
import com.torrydo.weathervisualizer.common.base.WithLazyComposeVar
import com.torrydo.weathervisualizer.ui.MainRoute
import com.torrydo.weathervisualizer.ui.assets.IconProvider
import com.torrydo.weathervisualizer.ui.theme.BLUE_LIGHTER
import com.torrydo.weathervisualizer.ui.theme.DARK_BLUE
import com.torrydo.weathervisualizer.ui.theme.LIGHT_BLUE
import com.torrydo.weathervisualizer.ui.theme.MyColor
import com.torrydo.weathervisualizer.utils.andr.showShortToast
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun CurrentWeatherScreen(mainNavController: NavController) = WithLazyComposeVar {

    context = initVar()
    scope = initVar()

    val viewModel: CurrentWeatherViewModel = getViewModel()

    val state = viewModel.collectAsState()

    viewModel.collectSideEffect {

        when (it) {
            is CurrentWeatherSideEffect.Toast -> {
                context.showShortToast("hello")
            }
            is CurrentWeatherSideEffect.NavigateToNext7DaysScreen -> {
                mainNavController.to(scope, MainRoute.Next7Days.route, data = state)
            }
        }
    }

    // UI ------------------------------------------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
    ) {
        viewModel.CardComponent(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .weight(1f),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Today", fontWeight = FontWeight.Bold)
                Text(
                    text = "Next 7 Days",
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    color = MyColor.DARK_BLUE,
                    modifier = Modifier.noRippleClickable {
                        viewModel.navigateToNext7DaysScreen()
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            viewModel.WeatherPerHourComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 40.dp)
                    .wrapContentHeight()
            )

        }

    }
}

@Composable
private fun CurrentWeatherViewModel.CardComponent(
    modifier: Modifier = Modifier,
    color: Color = MyColor.DARK_BLUE
) {

    val state = collectAsState().value

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4),
        backgroundColor = MyColor.LIGHT_BLUE,
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
                Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                    IconProvider.MarkerOutlined()
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = state.locationName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }

                Text(text = "Today ${state.time.toInt()}h", color = color)
            }

            Spacer(modifier = Modifier.height(10.dp))

//            Middle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${state.temperature}℃",
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    textAlign = TextAlign.Justify,
                    color = color,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = state.weatherType?.weatherDesc ?: "", fontSize = 20.sp, color = color)


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(text = "${state.pressure}hpa", color = color)
                    Text(text = "${state.humidity}%", color = color)
                    Text(text = "${state.windSpeed}km/h", color = color)

                }
            }

            TemperatureCardComponent(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .heightIn(min = 150.dp, max = 200.dp)
                    .clip(RoundedCornerShape(5))
                    .background(MyColor.BLUE_LIGHTER)

            )


        }
    }
}


@Composable
private fun CurrentWeatherViewModel.TemperatureCardComponent(
    modifier: Modifier = Modifier,
    color: Color = MyColor.DARK_BLUE
) {

    val state = collectAsState()

    val timeDecs = listOf("Morning", "Afternoon", "Evening", "Night")

    Column(modifier = modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
        Text(text = "Temperature", fontWeight = FontWeight.Bold, color = color, fontSize = 16.sp)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (state.value.weatherPerHour.isNotEmpty()) {
                timeDecs.forEachIndexed { index, s ->
                    val pos = (index + 1) * 6 - 1
                    Column(
                        modifier = Modifier.height(70.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = s, color = color)
                        Text(
                            text = "${state.value.weatherPerHour[pos].temperature}°C",
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun CurrentWeatherViewModel.WeatherPerHourComponent(
    modifier: Modifier = Modifier
) {

    val state = collectAsState()

    LazyRow(
        modifier = modifier
    ) {
        items(state.value.weatherPerHour) { item ->
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
