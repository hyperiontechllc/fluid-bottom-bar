package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.graphics.Color
import dev.hyperiontech.fluidbar.FluidBottomBarDefaults
import kotlin.test.Test
import kotlin.test.assertEquals

class SurfaceEmphasisTest {
    @Test
    fun of_keepsDarkEmphasisForDarkDefaults() {
        assertEquals(
            expected = SurfaceEmphasis.Dark,
            actual = SurfaceEmphasis.of(colors = FluidBottomBarDefaults.darkColors()),
        )
    }

    @Test
    fun of_usesLightEmphasisForLightDefaults() {
        assertEquals(
            expected = SurfaceEmphasis.Light,
            actual = SurfaceEmphasis.of(colors = FluidBottomBarDefaults.lightColors()),
        )
    }

    @Test
    fun of_detectsCustomLightSurface() {
        val colors =
            FluidBottomBarDefaults.darkColors(
                surfaceTop = Color(color = 0xFFFFF7ED),
                surfaceBottom = Color(color = 0xFFFFEDD5),
            )

        assertEquals(
            expected = SurfaceEmphasis.Light,
            actual = SurfaceEmphasis.of(colors = colors),
        )
    }

    @Test
    fun of_treatsMidDarkSurfaceAsDark() {
        val colors =
            FluidBottomBarDefaults.lightColors(
                surfaceTop = Color(color = 0xFF334155),
                surfaceBottom = Color(color = 0xFF1E293B),
            )

        assertEquals(expected = SurfaceEmphasis.Dark, actual = SurfaceEmphasis.of(colors = colors))
    }

    @Test
    fun of_blendsBothSurfaceColors() {
        val colors =
            FluidBottomBarDefaults.lightColors(
                surfaceTop = Color.White,
                surfaceBottom = Color.Black,
            )

        assertEquals(expected = SurfaceEmphasis.Dark, actual = SurfaceEmphasis.of(colors = colors))
    }
}
