package com.example.sparcs_new.ui.theme.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = theme_dark_primary,
    secondary = theme_dark_secondary,
    tertiary = theme_dark_tertiary,
    primaryContainer = theme_dark_primaryContainer,
    onPrimaryContainer = theme_dark_onPrimaryContainer,
    secondaryContainer = theme_dark_secondaryContainer,
    onSecondaryContainer = theme_dark_onSecondaryContainer,
    tertiaryContainer = theme_dark_tertiaryContainer,
    onTertiaryContainer = theme_dark_onTertiaryContainer,
    error = theme_dark_error,
    errorContainer = theme_dark_errorContainer,
    onError = theme_dark_onError,
    onErrorContainer = theme_dark_onErrorContainer,
    background = theme_dark_background,
    surface = theme_dark_surface,
    onPrimary = theme_dark_onPrimary,
    onSecondary = theme_dark_onSecondary,
    onTertiary = theme_dark_onTertiary,
    onBackground = theme_dark_onBackground,
    onSurface = theme_dark_onSurface,
    scrim = theme_dark_scrim,

    surfaceVariant = theme_dark_surfaceVariant,
    onSurfaceVariant = theme_dark_onSurfaceVariant,
    outline = theme_dark_outline,
    outlineVariant = theme_dark_outlineVariant,
    inversePrimary = theme_dark_inversePrimary,
    surfaceTint = theme_dark_surfaceTint,
    inverseSurface = theme_dark_inverseSurface,
    inverseOnSurface = theme_dark_inverseOnSurface

)

private val LightColorScheme = lightColorScheme(
    primary = theme_light_primary,
    secondary = theme_light_secondary,
    tertiary = theme_light_tertiary,
    primaryContainer = theme_light_primaryContainer,
    onPrimaryContainer = theme_light_onPrimaryContainer,
    secondaryContainer = theme_light_secondaryContainer,
    onSecondaryContainer = theme_light_onSecondaryContainer,
    tertiaryContainer = theme_light_tertiaryContainer,
    onTertiaryContainer = theme_light_onTertiaryContainer,
    error = theme_light_error,
    errorContainer = theme_light_errorContainer,
    onError = theme_light_onError,
    onErrorContainer = theme_light_onErrorContainer,
    background = theme_light_background,
    surface = theme_light_surface,
    onPrimary = theme_light_onPrimary,
    onSecondary = theme_light_onSecondary,
    onTertiary = theme_light_onTertiary,
    onBackground = theme_light_onBackground,
    onSurface = theme_light_onSurface,
    scrim = theme_light_scrim,
    surfaceVariant = theme_light_surfaceVariant,
    onSurfaceVariant = theme_light_onSurfaceVariant,
    outline = theme_light_outline,
    outlineVariant = theme_light_outlineVariant,
    inversePrimary = theme_light_inversePrimary,
    surfaceTint = theme_light_surfaceTint,
    inverseSurface = theme_light_inverseSurface,
    inverseOnSurface = theme_light_inverseOnSurface

)

@Composable
fun Sparcs_newTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private fun setUpEdgeToEdge(view: View, darkTheme: Boolean) {
    val window = (view.context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.Transparent.toArgb()
    val navigationBarColor = when {
        Build.VERSION.SDK_INT >= 29 -> Color.Transparent.toArgb()
        Build.VERSION.SDK_INT >= 26 -> Color(0xFF, 0xFF, 0xFF, 0x63).toArgb()
        // Min sdk version for this app is 24, this block is for SDK versions 24 and 25
        else -> Color(0x00, 0x00, 0x00, 0x50).toArgb()
    }
    window.navigationBarColor = navigationBarColor
    val controller = WindowCompat.getInsetsController(window, view)
    controller.isAppearanceLightStatusBars = !darkTheme
    controller.isAppearanceLightNavigationBars = !darkTheme
}