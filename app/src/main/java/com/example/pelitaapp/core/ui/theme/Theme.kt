package com.example.pelitaapp.core.ui.theme

// core/ui/theme/Theme.kt
package com.example.pelitaapp.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors: ColorScheme = lightColorScheme(
    primary = PelitaPrimary,
    onPrimary = PelitaOnPrimary,

    secondary = PelitaSecondary,
    onSecondary = PelitaOnSecondary,

    background = PelitaBackgroundLight,
    onBackground = PelitaOnBackgroundLight,

    surface = PelitaSurfaceLight,
    onSurface = PelitaOnSurfaceLight,

    error = PelitaError,
    onError = PelitaOnError
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = PelitaPrimary,
    onPrimary = PelitaOnPrimary,

    secondary = PelitaSecondary,
    onSecondary = PelitaOnSecondary,

    background = PelitaBackgroundDark,
    onBackground = PelitaOnBackgroundDark,

    surface = PelitaSurfaceDark,
    onSurface = PelitaOnSurfaceDark,

    error = PelitaError,
    onError = PelitaOnError
)

/**
 * PelitaTheme
 *
 * - darkTheme: kalau null => ikut sistem.
 * - dynamicColor: aktif jika Android 12+ (Material You).
 */
@Composable
fun PelitaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = PelitaTypography,
        content = content
    )
}