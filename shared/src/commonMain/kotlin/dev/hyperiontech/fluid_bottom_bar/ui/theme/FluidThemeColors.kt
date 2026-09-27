package dev.hyperiontech.fluid_bottom_bar.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
internal data class FluidThemeColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textTertiary: Color,
    val glassBorder: Color,
    val meshIntensity: Float,
)

private val Slate900 = Color(color = 0xFF0F172A)

internal val DarkThemeColors =
    FluidThemeColors(
        isDark = true,
        background = Color(color = 0xFF080815),
        surface = Color(color = 0xFF1E2140),
        textPrimary = Color.White,
        textTertiary = Color(color = 0xFF94A3B8),
        glassBorder = Color.White.copy(alpha = 0.16f),
        meshIntensity = 1f,
    )

internal val LightThemeColors =
    FluidThemeColors(
        isDark = false,
        background = Color(color = 0xFFF4F5FB),
        surface = Color.White,
        textPrimary = Slate900,
        textTertiary = Color(color = 0xFF64748B),
        glassBorder = Slate900.copy(alpha = 0.08f),
        meshIntensity = 0.55f,
    )

internal val LocalFluidThemeColors = compositionLocalOf { DarkThemeColors }
