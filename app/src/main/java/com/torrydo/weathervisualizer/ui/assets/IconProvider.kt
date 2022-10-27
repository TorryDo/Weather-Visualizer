package com.torrydo.weathervisualizer.ui.assets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.torrydo.weathervisualizer.R
import com.torrydo.weathervisualizer.utils.compose.noRippleClickable


@Composable
fun fromDrawable(
    @DrawableRes
    id: Int
) = ImageVector.vectorResource(id = id)

data class IconProps(
    val size: Dp = 20.dp,
    val color: Color = Color.Black
)

private val defaultIconProps = IconProps()

object IconProvider {


    @Composable
    fun getThemeColor() = MaterialTheme.colors.onSecondary

    private val DEFAULT_ICON_SIZE = 20.dp

    @Composable
    fun MarkerOutlined(modifier: Modifier = Modifier, iconProps: IconProps = defaultIconProps) {
        Image(
            modifier = modifier,
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_marker),
            contentDescription = "Color.UpArrow"
        )
    }

//    @Composable
//    fun Color.UpArrow(modifier: Modifier = Modifier) {
//        Image(
//            modifier = modifier,
//            imageVector = ImageVector.vectorResource(id = R.drawable.ic_color_up_arrow),
//            contentDescription = "Color.UpArrow"
//        )
//    }

}

@Composable
inline fun ClickableIcon(
    modifier: Modifier = Modifier,
    crossinline icon: @Composable () -> Unit,
    crossinline onClick: () -> Unit = {}
) {
    IconButton(modifier = modifier, onClick = { onClick() }) {
        icon()
    }
}

@Composable
fun StaticIcon(
    modifier: Modifier = Modifier.size(20.dp),
    icon: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}