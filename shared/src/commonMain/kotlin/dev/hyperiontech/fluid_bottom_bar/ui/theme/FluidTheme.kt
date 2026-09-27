package dev.hyperiontech.fluid_bottom_bar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val DarkColorScheme =
    darkColorScheme(
        primary = FluidColors.Purple500,
        onPrimary = DarkThemeColors.textPrimary,
        secondary = FluidColors.Cyan400,
        background = DarkThemeColors.background,
        onBackground = DarkThemeColors.textPrimary,
        surface = DarkThemeColors.surface,
        onSurface = DarkThemeColors.textPrimary,
        onSurfaceVariant = DarkThemeColors.textTertiary,
        outline = DarkThemeColors.glassBorder,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = FluidColors.Purple600,
        onPrimary = Color.White,
        secondary = FluidColors.Cyan400,
        background = LightThemeColors.background,
        onBackground = LightThemeColors.textPrimary,
        surface = LightThemeColors.surface,
        onSurface = LightThemeColors.textPrimary,
        onSurfaceVariant = LightThemeColors.textTertiary,
        outline = LightThemeColors.glassBorder,
    )

private val FluidTypography =
    Typography(
        headlineMedium =
            TextStyle(
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.025).em,
            ),
        titleLarge =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
            ),
        titleMedium =
            TextStyle(
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.015).em,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodySmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.05.em,
            ),
        labelMedium =
            TextStyle(
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelSmall =
            TextStyle(
                fontSize = 10.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.05.em,
            ),
    )

@Composable
fun FluidTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeColors = if (isDarkTheme) DarkThemeColors else LightThemeColors
    CompositionLocalProvider(LocalFluidThemeColors provides themeColors) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
            typography = FluidTypography,
            content = content,
        )
    }
}

internal object FluidTheme {
    val colors: FluidThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFluidThemeColors.current
}
