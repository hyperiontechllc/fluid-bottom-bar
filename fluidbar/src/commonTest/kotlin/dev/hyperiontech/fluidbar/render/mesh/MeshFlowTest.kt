package dev.hyperiontech.fluidbar.render.mesh

import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MeshFlowTest {
    private val wave = DriftWave(frequency = 2f, offset = 0.5f)

    @Test
    fun drift_followsSineOfPhaseScaledByIntensity() {
        val flow = MeshFlow(phase = 1f, intensity = 0.5f)

        assertEquals(expected = sin(x = 2.5f) * 0.5f, actual = flow.drift(wave = wave))
    }

    @Test
    fun drift_isStillWithoutIntensity() {
        val flow = MeshFlow(phase = 1f, intensity = 0f)

        assertEquals(expected = 0f, actual = flow.drift(wave = wave))
    }

    @Test
    fun drift_staysWithinIntensity() {
        val intensity = 0.35f

        (0..100).forEach { step ->
            val drift = MeshFlow(phase = step * 0.1f, intensity = intensity).drift(wave = wave)

            assertTrue(
                actual = drift in -intensity..intensity,
                message = "drift $drift exceeds intensity",
            )
        }
    }
}
