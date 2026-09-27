package dev.hyperiontech.fluidbar.geometry

import androidx.compose.ui.unit.Density
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlobRadiusTest {
    private val density = Density(density = 2f)
    private val restingRadius =
        with(receiver = density) { BAR_HEIGHT / 2f + FluidBottomBarTokens.BlobOverflow.toPx() }

    @Test
    fun blobRadius_atRestOverflowsHalfTheBarHeight() {
        val radius = with(receiver = density) { blobRadius(barHeight = BAR_HEIGHT, press = 0f) }

        assertEquals(expected = restingRadius, actual = radius)
        assertTrue(actual = radius > BAR_HEIGHT / 2f)
    }

    @Test
    fun blobRadius_growsByPressGrowWhenFullyPressed() {
        val radius = with(receiver = density) { blobRadius(barHeight = BAR_HEIGHT, press = 1f) }

        assertEquals(
            expected = restingRadius * (1f + FluidBottomBarTokens.PRESS_GROW),
            actual = radius,
            absoluteTolerance = TOLERANCE,
        )
    }

    @Test
    fun blobRadius_clampsPressOutsideTheUnitRange() {
        fun radiusAt(press: Float) =
            with(receiver = density) {
                blobRadius(barHeight = BAR_HEIGHT, press = press)
            }

        assertEquals(expected = radiusAt(press = 0f), actual = radiusAt(press = -0.5f))
        assertEquals(expected = radiusAt(press = 1f), actual = radiusAt(press = 1.5f))
    }

    private companion object {
        const val BAR_HEIGHT = 132f
        const val TOLERANCE = 1e-3f
    }
}
