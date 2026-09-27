package dev.hyperiontech.fluidbar.geometry

import kotlin.math.max
import kotlin.math.min

internal data class FluidShape(
    val left: Float,
    val right: Float,
    val bandHalfHeight: Float,
    val blobCenterX: Float,
    val blobRadiusX: Float,
    val blobRadiusY: Float,
    val blobLean: Float,
    val neckSmoothing: Float,
) {
    val leftCapCenter: Float get() = left + bandHalfHeight
    val rightCapCenter: Float get() = right - bandHalfHeight
    val blobLeftRadiusX: Float get() = blobRadiusX * (1f - blobLean)
    val blobRightRadiusX: Float get() = blobRadiusX * (1f + blobLean)
    val blobReachX: Float get() = max(a = blobLeftRadiusX, b = blobRightRadiusX)
    val startX: Float get() = min(a = left, b = blobCenterX - blobLeftRadiusX)
    val endX: Float get() = max(a = right, b = blobCenterX + blobRightRadiusX)

    fun blobRadiusXAt(x: Float): Float = if (x < blobCenterX) blobLeftRadiusX else blobRightRadiusX
}

internal data class FluidWave(
    val amplitude: Float,
    val wavelength: Float,
    val phase: Float,
    val ramp: Float,
    val distanceFalloff: Float,
    val seed: Float,
)
