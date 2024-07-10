package com.example.mytodolist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

val LocalColors = staticCompositionLocalOf { DarkThemeColors }
val LocalTypography = staticCompositionLocalOf { Typography }

@Composable
fun MyToDoListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    val LightColorScheme = lightColorScheme(
        primary = colors.backPrimary,
        onPrimary = colors.colorBlue,
        surface = colors.backSecondary,
        onSurface = colors.labelPrimary,
        onSurfaceVariant = colors.colorBlue,
    )

    val DarkColorScheme = darkColorScheme(
        primary = colors.backPrimary,
        onPrimary = colors.colorBlue,
        surface = colors.backSecondary,
        onSurface = colors.labelPrimary,
        onSurfaceVariant = colors.colorBlue,
    )

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            content = content,
            colorScheme = colorScheme
        )
    }
}