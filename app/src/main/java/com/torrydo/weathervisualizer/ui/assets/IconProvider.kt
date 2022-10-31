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
import com.torrydo.compose_easier.ext.noRippleClickable
import com.torrydo.weathervisualizer.R


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

    @Composable
    fun LeftArrow(modifier: Modifier = Modifier, iconProps: IconProps = defaultIconProps) {
        Image(
            modifier = modifier,
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_left_arrow),
            contentDescription = "Color.UpArrow"
        )
    }

}
