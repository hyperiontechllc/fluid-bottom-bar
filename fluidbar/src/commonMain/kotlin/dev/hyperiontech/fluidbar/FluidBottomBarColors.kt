package dev.hyperiontech.fluidbar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.fluidbar.theme.FluidColorTokens

@Immutable
public data class FluidBottomBarColors(
    public val surfaceTop: Color,
    public val surfaceBottom: Color,
    public val selectedIcon: Color,
    public val unselectedIcon: Color,
    public val shadow: Color,
)

public object FluidBottomBarDefaults {
    @Composable
    @ReadOnlyComposable
    public fun colors(): FluidBottomBarColors =
        if (isSystemInDarkTheme()) {
            darkColors()
        } else {
            lightColors()
        }

    public fun darkColors(
        surfaceTop: Color = FluidColorTokens.DarkSurfaceTop,
        surfaceBottom: Color = FluidColorTokens.DarkSurfaceBottom,
        selectedIcon: Color = FluidColorTokens.DarkSelectedIcon,
        unselectedIcon: Color = FluidColorTokens.DarkUnselectedIcon,
        shadow: Color = FluidColorTokens.DarkShadow,
    ): FluidBottomBarColors =
        FluidBottomBarColors(
            surfaceTop = surfaceTop,
            surfaceBottom = surfaceBottom,
            selectedIcon = selectedIcon,
            unselectedIcon = unselectedIcon,
            shadow = shadow,
        )

    public fun lightColors(
        surfaceTop: Color = FluidColorTokens.LightSurfaceTop,
        surfaceBottom: Color = FluidColorTokens.LightSurfaceBottom,
        selectedIcon: Color = FluidColorTokens.LightSelectedIcon,
        unselectedIcon: Color = FluidColorTokens.LightUnselectedIcon,
        shadow: Color = FluidColorTokens.LightShadow,
    ): FluidBottomBarColors =
        FluidBottomBarColors(
            surfaceTop = surfaceTop,
            surfaceBottom = surfaceBottom,
            selectedIcon = selectedIcon,
            unselectedIcon = unselectedIcon,
            shadow = shadow,
        )
}
