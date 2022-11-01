package com.torrydo.weathervisualizer.ui.screen.weather_map

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.torrydo.weathervisualizer.common.base.LazyComposeVar
import com.torrydo.weathervisualizer.common.base.WithLazyComposeVar
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.utils.GoogleMapStyle
import com.torrydo.weathervisualizer.utils.Logger
import com.torrydo.weathervisualizer.utils.andr.showShortToast
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun WeatherMapScreen() = WithLazyComposeVar {

    val viewModel: WeatherMapViewModel = getViewModel()

    context = initVar()
    scope = initVar()

//    val state by viewModel.collectAsState()

    var keyMapClick by remember { mutableStateOf("") }

    viewModel.collectSideEffect {
        when (it) {
            is WeatherMapSideEffect.Toast -> {
                context.showShortToast("toasting")
            }
            is WeatherMapSideEffect.OnMapClick -> {
//                keyMapClick = ""
            }
        }
    }


    // UI ------------------------------------------------------------------------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {

        if (viewModel.weatherDataList.isNotEmpty()) {

            val curLatLng = viewModel.weatherDataList[0].let { LatLng(it.lat, it.lng) }

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(curLatLng, 10f)
            }
            val captureController = rememberCaptureController()

            var bm: Bitmap? by remember { mutableStateOf(null) }

            var pos: Int = remember { 0 }

            Capturable(
                controller = captureController,
                onCaptured = { bitmap, error ->
                    // This is captured bitmap of a content inside Capturable Composable.
                    if (bitmap != null) {
                        // Bitmap is captured successfully. Do something with it!
                        bm = bitmap.asAndroidBitmap()
                    }

                    if (error != null) {
                        // Error occurred. Handle it!
                        Logger.e(error.stackTraceToString())
                    }
                }
            ) {
                CustomMarker(weatherData = viewModel.weatherDataList[pos])
            }


            GoogleMap(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.small),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapStyleOptions = MapStyleOptions(GoogleMapStyle.SIMPLE)),
                onMapClick = viewModel::onMapCLick,
            ) {

                viewModel.weatherDataList.forEachIndexed { index, weatherData ->

                    val markerState =
                        rememberMarkerState(null, LatLng(weatherData.lat, weatherData.lng))

                    MarkerInfoWindow(
                        state = markerState,
                        title = "City",
                        snippet = "\uD83C\uDF1E ${weatherData.temperature}",
                        icon = bm?.let { BitmapDescriptorFactory.fromBitmap(it) }
//                        alpha = 0f,
//                        infoWindowAnchor = Offset(0.5f, 1.0f)

                    )

                    SideEffect {
                        pos = index
                        captureController.capture()
//                        markerState.showInfoWindow()
                    }

                }

            }
        }

        // autocomplete ----------------------------------------------------------------------------


    }
}

@Composable
private fun LazyComposeVar.CustomMarker(
    weatherData: WeatherData
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(15))
            .background(Color.White)
            .padding(vertical = 10.dp)
            .shadow(elevation = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "Gotham", fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(
            text = "\uD83C\uDF1E ${weatherData.temperature}",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}