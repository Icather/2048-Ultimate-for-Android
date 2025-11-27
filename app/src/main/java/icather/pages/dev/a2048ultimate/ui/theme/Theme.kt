package icather.pages.dev.a2048ultimate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ButtonBackground,
    secondary = GridBackground,
    tertiary = Tile2048,
    background = GameBackground,
    surface = GridBackground,
    onPrimary = ButtonText,
    onSecondary = TextWhite,
    onTertiary = TextWhite,
    onBackground = TextColor,
    onSurface = TextWhite
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonBackground,
    secondary = GridBackground,
    tertiary = Tile2048,
    background = GameBackground,
    surface = GridBackground,
    onPrimary = ButtonText,
    onSecondary = TextWhite,
    onTertiary = TextWhite,
    onBackground = TextColor,
    onSurface = TextWhite
)

@Composable
fun _2048UltimateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color to enforce 2048 theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}