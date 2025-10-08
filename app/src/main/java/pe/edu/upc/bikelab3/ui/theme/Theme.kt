package pe.edu.upc.bikelab3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = GreenBike,
    onPrimary = Color.White,
    secondary = GreenDark,
    background = GrayBackground,
    onBackground = TextGray,
)

@Composable
fun BikeLab3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
