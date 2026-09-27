package dev.hyperiontech.fluidbar.geometry

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class FluidGeometryTest {
    private val shape =
        FluidShape(
            left = 5f,
            right = 359f,
            bandHalfHeight = 28f,
            blobCenterX = 180f,
            blobRadiusX = 39f,
            blobRadiusY = 39f,
            blobLean = 0f,
            neckSmoothing = NECK_SMOOTHING,
        )

    private val calmWave =
        FluidWave(
            amplitude = 0f,
            wavelength = 72f,
            phase = 0f,
            ramp = WAVE_RAMP,
            distanceFalloff = 0.6f,
            seed = 0f,
        )

    private val excitedWave = calmWave.copy(amplitude = WAVE_AMPLITUDE, phase = 0.7f)

    @Test
    fun cosineSampleX_coversRangeAndConcentratesAtEnds() {
        val first = FluidGeometry.cosineSampleX(fraction = 0f, start = 0f, end = 100f)
        val last = FluidGeometry.cosineSampleX(fraction = 1f, start = 0f, end = 100f)
        val nearStart = FluidGeometry.cosineSampleX(fraction = 0.01f, start = 0f, end = 100f)
        val middle = FluidGeometry.cosineSampleX(fraction = 0.5f, start = 0f, end = 100f)
        val nearMiddle = FluidGeometry.cosineSampleX(fraction = 0.51f, start = 0f, end = 100f)

        assertEquals(expected = 0f, actual = first)
        assertEquals(expected = 100f, actual = last, absoluteTolerance = TOLERANCE)
        assertTrue(actual = nearStart - first < nearMiddle - middle)
    }

    @Test
    fun bandHalfHeight_endsFollowSemicircleConcentricWithCaps() {
        listOf(0.1f, 0.5f, 0.9f).forEach { ratio ->
            val radius = shape.bandHalfHeight
            val distance = radius * ratio
            val expected = sqrt(x = radius * radius - distance * distance)

            val atLeftCap =
                FluidGeometry.bandHalfHeight(x = shape.leftCapCenter - distance, shape = shape)
            val atRightCap =
                FluidGeometry.bandHalfHeight(x = shape.rightCapCenter + distance, shape = shape)

            assertEquals(expected = expected, actual = atLeftCap, absoluteTolerance = TOLERANCE)
            assertEquals(expected = expected, actual = atRightCap, absoluteTolerance = TOLERANCE)
        }
    }

    @Test
    fun bandHalfHeight_closesAtBandEdges() {
        assertEquals(
            expected = 0f,
            actual = FluidGeometry.bandHalfHeight(x = shape.left, shape = shape),
        )
        assertEquals(
            expected = 0f,
            actual = FluidGeometry.bandHalfHeight(x = shape.right, shape = shape),
        )
    }

    @Test
    fun bandHalfHeight_isFlatBetweenCaps() {
        val middle = (shape.leftCapCenter + shape.rightCapCenter) / 2

        assertEquals(
            expected = shape.bandHalfHeight,
            actual = FluidGeometry.bandHalfHeight(x = middle, shape = shape),
        )
    }

    @Test
    fun blobHalfHeight_formsEllipseAroundCenter() {
        val atCenter = FluidGeometry.blobHalfHeight(x = shape.blobCenterX, shape = shape)
        val atEdge =
            FluidGeometry.blobHalfHeight(x = shape.blobCenterX + shape.blobRadiusX, shape = shape)

        assertEquals(expected = shape.blobRadiusY, actual = atCenter)
        assertEquals(expected = 0f, actual = atEdge)
    }

    @Test
    fun blobHalfHeight_bulgesTowardsLeanDirection() {
        val leaningShape = shape.copy(blobLean = 0.2f)
        val distance = shape.blobRadiusX * 0.9f

        val leading =
            FluidGeometry.blobHalfHeight(x = shape.blobCenterX + distance, shape = leaningShape)
        val trailing =
            FluidGeometry.blobHalfHeight(x = shape.blobCenterX - distance, shape = leaningShape)

        assertTrue(actual = leading > 0f)
        assertEquals(expected = 0f, actual = trailing)
    }

    @Test
    fun edgeHalfHeight_bulgesBeyondBandAtSelection() {
        val atBlob =
            FluidGeometry.edgeHalfHeight(x = shape.blobCenterX, shape = shape, wave = calmWave)
        val onBand =
            FluidGeometry.edgeHalfHeight(x = shape.left + 100f, shape = shape, wave = calmWave)

        assertTrue(actual = atBlob >= shape.blobRadiusY)
        assertEquals(expected = shape.blobRadiusY, actual = atBlob, absoluteTolerance = 0.5f)
        assertEquals(
            expected = shape.bandHalfHeight,
            actual = onBand,
            absoluteTolerance = TOLERANCE,
        )
    }

    @Test
    fun smoothUnion_neverUndercutsEitherShape() {
        val values = listOf(0f, 5f, 20f, 28f, 30f, 39f)

        values.forEach { a ->
            values.forEach { b ->
                val union = FluidGeometry.smoothUnion(a = a, b = b, smoothing = NECK_SMOOTHING)
                val larger = maxOf(a = a, b = b)

                assertTrue(
                    actual = union >= larger - TOLERANCE,
                    message = "union $union below max($a, $b)",
                )
            }
        }
    }

    @Test
    fun smoothUnion_isExactWhenOneShapeIsAbsent() {
        assertEquals(
            expected = 28f,
            actual = FluidGeometry.smoothUnion(a = 28f, b = 0f, smoothing = NECK_SMOOTHING),
        )
        assertEquals(
            expected = 0f,
            actual = FluidGeometry.smoothUnion(a = 0f, b = 0f, smoothing = NECK_SMOOTHING),
        )
    }

    @Test
    fun smoothUnion_blendsNeckWhereShapesMeet() {
        val union = FluidGeometry.smoothUnion(a = 28f, b = 28f, smoothing = NECK_SMOOTHING)

        assertTrue(actual = union > 28f)
    }

    @Test
    fun waveWeight_isZeroInsideCapsAndBlob() {
        fun weightAt(x: Float) = FluidGeometry.waveWeight(x = x, shape = shape, ramp = WAVE_RAMP)

        assertEquals(expected = 0f, actual = weightAt(x = shape.leftCapCenter - 10f))
        assertEquals(expected = 0f, actual = weightAt(x = shape.rightCapCenter + 10f))
        assertEquals(expected = 0f, actual = weightAt(x = shape.blobCenterX))
        assertEquals(expected = 1f, actual = weightAt(x = shape.blobCenterX + 100f))
    }

    @Test
    fun waveOffset_leavesCapsCalmSoEndsMatchBarCurvature() {
        val capX = shape.leftCapCenter - shape.bandHalfHeight / 2

        val offset = FluidGeometry.waveOffset(x = capX, shape = shape, wave = excitedWave)
        val band = FluidGeometry.bandHalfHeight(x = capX, shape = shape)
        val edge = FluidGeometry.edgeHalfHeight(x = capX, shape = shape, wave = excitedWave)

        assertEquals(expected = 0f, actual = offset)
        assertEquals(expected = band, actual = edge, absoluteTolerance = TOLERANCE)
    }

    @Test
    fun waveOffset_staysWithinAmplitude() {
        (0..360).forEach { x ->
            val offset =
                FluidGeometry.waveOffset(x = x.toFloat(), shape = shape, wave = excitedWave)

            assertTrue(
                actual = offset in -WAVE_AMPLITUDE..WAVE_AMPLITUDE,
                message = "offset $offset exceeds amplitude at x=$x",
            )
        }
    }

    @Test
    fun waveOffset_isNotMirroredAroundSelection() {
        val distances = listOf(80f, 100f, 120f)

        fun offsetAt(x: Float) = FluidGeometry.waveOffset(x = x, shape = shape, wave = excitedWave)

        val mirrored =
            distances.all { distance ->
                val left = offsetAt(x = shape.blobCenterX - distance)
                val right = offsetAt(x = shape.blobCenterX + distance)
                abs(x = left - right) < TOLERANCE
            }

        assertFalse(actual = mirrored)
    }

    @Test
    fun seaSurface_hasSharperCrestsThanTroughs() {
        val heights =
            (0..20_000).map {
                FluidGeometry.seaSurface(x = it * 0.1f, wave = calmWave)
            }

        assertTrue(actual = heights.max() > -heights.min())
        assertTrue(actual = heights.all { it in -1f..1f })
    }

    @Test
    fun seaSurface_travelsWithPhase() {
        val resting = FluidGeometry.seaSurface(x = 30f, wave = calmWave)
        val advanced = FluidGeometry.seaSurface(x = 30f, wave = calmWave.copy(phase = 1f))

        assertNotEquals(illegal = resting, actual = advanced)
    }

    @Test
    fun seaSurface_differsBetweenSeeds() {
        val topEdge = FluidGeometry.seaSurface(x = 30f, wave = calmWave)
        val bottomEdge = FluidGeometry.seaSurface(x = 30f, wave = calmWave.copy(seed = 2.3f))

        assertNotEquals(illegal = topEdge, actual = bottomEdge)
    }

    private companion object {
        const val TOLERANCE = 1e-3f
        const val NECK_SMOOTHING = 14f
        const val WAVE_RAMP = 18f
        const val WAVE_AMPLITUDE = 3f
    }
}
