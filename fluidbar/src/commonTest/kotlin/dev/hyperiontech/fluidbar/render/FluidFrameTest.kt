package dev.hyperiontech.fluidbar.render

import dev.hyperiontech.fluidbar.geometry.FluidWave
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.test.Test
import kotlin.test.assertEquals

class FluidFrameTest {
    private val topWave =
        FluidWave(
            amplitude = 4f,
            wavelength = 100f,
            phase = 1.5f,
            ramp = 18f,
            distanceFalloff = 0.6f,
            seed = 0f,
        )

    @Test
    fun toBottomWave_scalesAmplitudeAndWavelength() {
        val bottomWave = topWave.toBottomWave()

        assertEquals(
            expected = topWave.amplitude * FluidBottomBarTokens.BOTTOM_WAVE_AMPLITUDE_RATIO,
            actual = bottomWave.amplitude,
        )
        assertEquals(
            expected = topWave.wavelength * FluidBottomBarTokens.BOTTOM_WAVE_LENGTH_RATIO,
            actual = bottomWave.wavelength,
        )
    }

    @Test
    fun toBottomWave_usesItsOwnSeed() {
        assertEquals(
            expected = FluidBottomBarTokens.BOTTOM_WAVE_SEED,
            actual = topWave.toBottomWave().seed,
        )
    }

    @Test
    fun toBottomWave_keepsPhaseRampAndFalloffInSync() {
        val bottomWave = topWave.toBottomWave()

        assertEquals(expected = topWave.phase, actual = bottomWave.phase)
        assertEquals(expected = topWave.ramp, actual = bottomWave.ramp)
        assertEquals(expected = topWave.distanceFalloff, actual = bottomWave.distanceFalloff)
    }
}
