@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)

package com.torrydo.weathervisualizer.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.torrydo.weathervisualizer.ui.screen.city_search.CitySearchScreen
import com.torrydo.weathervisualizer.ui.screen.next7days.Next7DaysScreen
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

    LaunchedEffect(Unit) {
        if (permission.status.isGranted.not()) {
            permission.launchPermissionRequest()
        }
    }

    if (permission.status.isGranted) {

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
    } else {
        LocationNotGrantedComponent()
    }

}

@Composable
private fun LocationNotGrantedComponent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text(text = "Location permission is not granted", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherVisualizerTheme {
        MainNav()
    }
}