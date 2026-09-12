package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MakimaDarkColorScheme = darkColorScheme(
    primary = MakimaCrimson,
    onPrimary = MakimaTextPrimary,
    primaryContainer = MakimaDarkRed,
    secondary = MakimaGold,
    onSecondary = MakimaObsidianDark,
    background = MakimaObsidianDark,
    onBackground = MakimaTextPrimary,
    surface = MakimaSurfaceDark,
    onSurface = MakimaTextPrimary,
    surfaceVariant = MakimaSurfaceElevated,
    onSurfaceVariant = MakimaTextSecondary,
    outline = MakimaBorderGlow,
    error = MakimaStatusRed,
    tertiary = MakimaRose
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MakimaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
