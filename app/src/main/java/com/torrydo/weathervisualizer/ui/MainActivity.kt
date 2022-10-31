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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.torrydo.compose_easier.navigation.Nav
import com.torrydo.compose_easier.navigation.NavItem
import com.torrydo.weathervisualizer.ui.screen.home_screen.HomeScreen
import com.torrydo.weathervisualizer.ui.screen.next7days.Next7DaysScreen
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

sealed class MainRoute(route: String, title: String): NavItem(route, title) {
    object HomeScreen : MainRoute("homeScreen", "Home screen")
    object Next7Days : MainRoute("next7days", "Next 7 Days")
}

private val mainScreens = listOf(
    MainRoute.HomeScreen,
    MainRoute.Next7Days
)

@Composable
fun MainNav() {

    val permission = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    val navController = rememberAnimatedNavController()

    val nav = remember { Nav(navController, mainScreens) }

    LaunchedEffect(Unit) {
        if (permission.status.isGranted.not()) {
            permission.launchPermissionRequest()
        }
    }

    if (permission.status.isGranted) {

        nav.SetupNavGraph { route, _, data ->
            when (route) {
                MainRoute.HomeScreen.route -> HomeScreen(navController)
                MainRoute.Next7Days.route -> Next7DaysScreen()
            }
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