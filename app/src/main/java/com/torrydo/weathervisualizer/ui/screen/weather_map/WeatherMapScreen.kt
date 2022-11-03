package com.torrydo.weathervisualizer.ui.screen.weather_map

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.torrydo.compose_easier.view.IconEz
import com.torrydo.weathervisualizer.common.base.WithLazyComposeVar
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.ui.assets.fromDrawable
import com.torrydo.weathervisualizer.utils.CaptureBitmap
import com.torrydo.weathervisualizer.utils.GoogleMapStyle
import com.torrydo.weathervisualizer.utils.andr.showLongToast
import com.torrydo.weathervisualizer.utils.andr.showShortToast
import com.torrydo.weathervisualizer.utils.lang.compose.coloredShadow
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun WeatherMapScreen() = WithLazyComposeVar {

    context = initVar()
    scope = initVar()

    val vm: WeatherMapViewModel = getViewModel()

    val state = vm.collectAsState()

    vm.collectSideEffect{
        when(it){
            is WeatherMapSideEffect.OnMapClick -> {

            }
            is WeatherMapSideEffect.AlertCanNotRemoveCurrentMarker -> {
                context.showLongToast("you can't remove this marker bcause it's your current location")
            }
            is WeatherMapSideEffect.Toast -> {
                context.showShortToast(it.message)
            }
        }
    }

    // UI ------------------------------------------------------------------------------------------

    state.value.weathers.apply {
        this.forEach {
            val ll = it.getLatLng()
            if (vm.markerAndBmMap[ll] == null) {
                vm.markerAndBmMap[ll] = vm.getBitmapByWeather(it).invoke()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        state.value.weathers.let { weathers ->

            if (weathers.isNotEmpty()) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        weathers[0].getLatLng(), 10f
                    )
                }

                GoogleMap(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(mapStyleOptions = MapStyleOptions(GoogleMapStyle.SIMPLE)),
                    onMapClick = vm::addMarker
                ) {

                    for (ll in state.value.weathers.map { it.getLatLng() }) {
                        MarkerInfoWindow(
                            state = MarkerState(ll),
                            icon = vm.markerAndBmMap[ll]?.let {
                                BitmapDescriptorFactory.fromBitmap(
                                    it
                                )
                            },
                            onClick = {
                                vm.removeMarker(ll)
                                true
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherMapViewModel.getBitmapByWeather(cur: WeatherData?): () -> Bitmap? {

    if (cur == null) return { null }

    return CaptureBitmap {

        CustomMarker(backgroundColor = Color.LightGray) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(text = cur.weatherType.weatherDesc, fontWeight = FontWeight.Bold, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconEz.Static(
                        icon = {
                            cur.weatherType.let {
                                Image(
                                    imageVector = fromDrawable(id = it.iconRes),
                                    contentDescription = it.weatherDesc,
                                )
                            }
                        }
                    )
                    Text(
                        text = " ${cur.temperature}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

    }
}

@Composable
private fun CustomMarker(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    contentPadding: PaddingValues = PaddingValues(vertical = 5.dp, horizontal = 10.dp),
    content: @Composable BoxScope.() -> Unit
) {

    val path = Path()

    val triangleSize = DpSize(width = 10.dp, height = 10.dp)

    Box(
        modifier = modifier
            .padding(5.dp)
            .coloredShadow(
                color = Color.Black.copy(alpha = 0.5f),
                blurRadius = 10.dp,
                borderRadius = 5.dp,
                spread = -7f,
            )
            .drawBehind {

                val rect = Rect(Offset.Zero, size)

                drawRoundRect(
                    color = backgroundColor,
                    size = size.copy(height = size.height - triangleSize.height.toPx()),
                    cornerRadius = CornerRadius(15f, 15f)
                )

                path.apply {
                    moveTo(
                        x = rect.bottomCenter.x - triangleSize.width.toPx(),
                        y = rect.bottomCenter.y - triangleSize.height.toPx() - 1
                    )
                    lineTo(
                        x = rect.bottomCenter.x + triangleSize.width.toPx(),
                        y = rect.bottomCenter.y - triangleSize.height.toPx() - 1
                    )

                    lineTo(
                        x = rect.bottomCenter.x,
                        y = rect.bottomCenter.y
                    )
                    lineTo(
                        x = rect.bottomCenter.x - triangleSize.width.toPx(),
                        y = rect.bottomCenter.y - triangleSize.height.toPx() - 1
                    )
                }
                drawPath(path, backgroundColor)


            }
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = triangleSize.height + contentPadding.calculateBottomPadding(),
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
            ),
        content = content
    )
}
