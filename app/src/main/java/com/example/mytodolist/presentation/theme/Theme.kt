package com.example.mytodolist.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalColors = staticCompositionLocalOf { DarkThemeColors }

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

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}