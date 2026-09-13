package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = FireOrangePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF421508),
    onPrimaryContainer = FireOrangeLight,
    secondary = NeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF003840),
    onSecondaryContainer = NeonCyan,
    tertiary = GoldCoin,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF453000),
    onTertiaryContainer = GoldCoinGlow,
    background = GameDarkBackground,
    onBackground = TextWhite,
    surface = GameDarkSurface,
    onSurface = TextWhite,
    surfaceVariant = GameDarkSurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = GameCardBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
