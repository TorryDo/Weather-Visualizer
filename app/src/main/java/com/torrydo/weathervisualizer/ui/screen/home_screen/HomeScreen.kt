package com.torrydo.weathervisualizer.ui.screen.home_screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.torrydo.compose_easier.navigation.BottomNavBar
import com.torrydo.compose_easier.navigation.BottomNavItem
import com.torrydo.weathervisualizer.ui.screen.weather_map.WeatherMapScreen
import com.torrydo.weathervisualizer.ui.screen.current_weather.CurrentWeatherScreen
import com.torrydo.weathervisualizer.ui.theme.LIGHT_BLUE
import com.torrydo.weathervisualizer.ui.theme.MyColor


sealed class HomeRoute(
    route: String,
    title: String,
    icon: ImageVector
) : BottomNavItem(route, title, icon) {

    object CurrentWeather : HomeRoute("current_weather", "Current Weather", Icons.Default.Home)

    object CitySearch : HomeRoute("city_search", "City Search", Icons.Default.Search)

}

private var homeScreens = listOf(
    HomeRoute.CurrentWeather,
    HomeRoute.CitySearch
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(mainNavController: NavController ) {

    val navHostController = rememberAnimatedNavController()

    val bottomNavBar = remember { BottomNavBar(navHostController, homeScreens) }


    Column {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Black)
        ) {

            AnimatedNavHost(
                navController = navHostController,
                startDestination = HomeRoute.CurrentWeather.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                homeScreens.forEach { navItem ->
                    composable(
                        route = navItem.route,
                    ) {
                        when (navItem.route) {
                            HomeRoute.CurrentWeather.route -> CurrentWeatherScreen(mainNavController)
                            HomeRoute.CitySearch.route -> WeatherMapScreen()
                        }
                    }
                }
            }
        }

        bottomNavBar.Render(backgroundColor = MyColor.LIGHT_BLUE)
    }


}