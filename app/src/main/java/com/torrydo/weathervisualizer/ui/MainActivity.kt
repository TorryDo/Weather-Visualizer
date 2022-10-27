@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)

package com.torrydo.weathervisualizer.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.torrydo.weathervisualizer.ui.screen.next7days.Next7DaysScreen
import com.torrydo.weathervisualizer.ui.screen.city_search.CitySearchScreen
import com.torrydo.weathervisualizer.ui.screen.weather_today.WeatherTodayScreen
import com.torrydo.weathervisualizer.ui.theme.WeatherVisualizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherVisualizerTheme {
                MainNav()
            }
        }
    }
}

sealed class Route(val name: String, val icon: ImageVector) {
    object WeatherToday : Route("weather_today", Icons.Default.Home)
    object CitySearch : Route("city_search", Icons.Default.Search)
    object Next7Days : Route("next7days", Icons.Default.Menu)
}

@Composable
fun MainNav() {

    val context = LocalContext.current

    val permission = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    val navController = rememberAnimatedNavController()

//    LaunchedEffect(null) {
//        when (permission.status) {
//            is PermissionStatus.Denied -> {
//                permission.launchPermissionRequest()
//            }
//            is PermissionStatus.Granted -> {
//                context.showShortToast("location granted")
//            }
//        }
//    }

    AnimatedNavHost(navController = navController, startDestination = Route.WeatherToday.name) {
        composable(
            route = Route.WeatherToday.name,
            content = { WeatherTodayScreen() }
        )
        composable(
            route = Route.CitySearch.name,
            content = { CitySearchScreen() }
        )
        composable(
            route = Route.Next7Days.name,
            content = { Next7DaysScreen() }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherVisualizerTheme {
        MainNav()
    }
}