package dev.hyperiontech.fluidbar.geometry

import kotlin.test.Test
import kotlin.test.assertEquals

class FluidShapeTest {
    private val shape =
        FluidShape(
            left = 0f,
            right = 300f,
            bandHalfHeight = 30f,
            blobCenterX = 150f,
            blobRadiusX = BLOB_RADIUS_X,
            blobRadiusY = 40f,
            blobLean = 0f,
            neckSmoothing = 14f,
        )

    @Test
    fun capCenters_sitOneBandHalfHeightInsideTheEdges() {
        assertEquals(expected = 30f, actual = shape.leftCapCenter)
        assertEquals(expected = 270f, actual = shape.rightCapCenter)
    }

    @Test
    fun blobRadii_areSymmetricWithoutLean() {
        assertEquals(expected = BLOB_RADIUS_X, actual = shape.blobLeftRadiusX)
        assertEquals(expected = BLOB_RADIUS_X, actual = shape.blobRightRadiusX)
        assertEquals(expected = BLOB_RADIUS_X, actual = shape.blobReachX)
    }

    @Test
    fun positiveLean_growsRightSideAndShrinksLeftSide() {
        val leaning = shape.copy(blobLean = LEAN)

        assertEquals(expected = BLOB_RADIUS_X * (1f - LEAN), actual = leaning.blobLeftRadiusX)
        assertEquals(expected = BLOB_RADIUS_X * (1f + LEAN), actual = leaning.blobRightRadiusX)
        assertEquals(expected = leaning.blobRightRadiusX, actual = leaning.blobReachX)
    }

    @Test
    fun negativeLean_growsLeftSide() {
        val leaning = shape.copy(blobLean = -LEAN)

        assertEquals(expected = leaning.blobLeftRadiusX, actual = leaning.blobReachX)
    }

    @Test
    fun blobRadiusXAt_usesTheRadiusOfTheSideItIsOn() {
        val leaning = shape.copy(blobLean = LEAN)

        assertEquals(expected = leaning.blobLeftRadiusX, actual = leaning.blobRadiusXAt(x = 100f))
        assertEquals(expected = leaning.blobRightRadiusX, actual = leaning.blobRadiusXAt(x = 200f))
    }

    @Test
    fun extent_followsBandWhenBlobStaysInside() {
        assertEquals(expected = shape.left, actual = shape.startX)
        assertEquals(expected = shape.right, actual = shape.endX)
    }

    @Test
    fun extent_growsWhenBlobOverflowsTheBand() {
        val atStart = shape.copy(blobCenterX = 10f)
        val atEnd = shape.copy(blobCenterX = 290f)

        assertEquals(expected = 10f - BLOB_RADIUS_X, actual = atStart.startX)
        assertEquals(expected = 290f + BLOB_RADIUS_X, actual = atEnd.endX)
    }

    private companion object {
        const val BLOB_RADIUS_X = 40f
        const val LEAN = 0.25f
    }
}
