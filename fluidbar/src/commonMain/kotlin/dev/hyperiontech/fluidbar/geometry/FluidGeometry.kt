package dev.hyperiontech.fluidbar.geometry

import androidx.compose.ui.unit.Density
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

private class SeaSwell(
    val wavelengthRatio: Float,
    val amplitude: Float,
    val offset: Float,
) {
    val dispersion: Float = sqrt(x = 1f / wavelengthRatio)
}

internal object FluidGeometry {
    private const val TWO_PI = (2 * PI).toFloat()
    private const val CREST_SHARPNESS = 0.15f
    private val SeaSwells =
        listOf(
            SeaSwell(wavelengthRatio = 1f, amplitude = 1f, offset = 0f),
            SeaSwell(wavelengthRatio = 0.55f, amplitude = 0.35f, offset = 1.7f),
        )
    private val SeaSwellNormalization =
        SeaSwells.sumOf { it.amplitude.toDouble() }.toFloat() * (1f + CREST_SHARPNESS)

    fun cosineSampleX(
        fraction: Float,
        start: Float,
        end: Float,
    ): Float {
        val easedFraction = (1f - cos(x = PI.toFloat() * fraction)) / 2f
        return start + (end - start) * easedFraction
    }

    fun edgeHalfHeight(
        x: Float,
        shape: FluidShape,
        wave: FluidWave,
    ): Float {
        val wavyBand =
            bandHalfHeight(
                x = x,
                shape = shape,
            ) + waveOffset(x = x, shape = shape, wave = wave)
        return smoothUnion(
            a = wavyBand.coerceAtLeast(minimumValue = 0f),
            b = blobHalfHeight(x = x, shape = shape),
            smoothing = shape.neckSmoothing,
        )
    }

    fun bandHalfHeight(
        x: Float,
        shape: FluidShape,
    ): Float =
        when {
            x <= shape.left || x >= shape.right -> {
                0f
            }

            x < shape.leftCapCenter -> {
                circleHalfHeight(
                    distance = shape.leftCapCenter - x,
                    radius = shape.bandHalfHeight,
                )
            }

            x > shape.rightCapCenter -> {
                circleHalfHeight(
                    distance = x - shape.rightCapCenter,
                    radius = shape.bandHalfHeight,
                )
            }

            else -> {
                shape.bandHalfHeight
            }
        }

    fun blobHalfHeight(
        x: Float,
        shape: FluidShape,
    ): Float {
        val normalizedDistance = (x - shape.blobCenterX) / shape.blobRadiusXAt(x = x)
        if (abs(x = normalizedDistance) >= 1f) return 0f
        return shape.blobRadiusY * sqrt(x = 1f - normalizedDistance * normalizedDistance)
    }

    fun smoothUnion(
        a: Float,
        b: Float,
        smoothing: Float,
    ): Float {
        val overlap =
            (min(a = a, b = b) / smoothing).coerceIn(
                minimumValue = 0f,
                maximumValue = 1f,
            )
        val effectiveSmoothing = smoothing * overlap
        if (effectiveSmoothing <= 0f) return max(a = a, b = b)
        val blend =
            (0.5f + 0.5f * (a - b) / effectiveSmoothing).coerceIn(
                minimumValue = 0f,
                maximumValue = 1f,
            )
        return b + (a - b) * blend + effectiveSmoothing * blend * (1f - blend)
    }

    fun waveOffset(
        x: Float,
        shape: FluidShape,
        wave: FluidWave,
    ): Float {
        val distance = abs(x = x - shape.blobCenterX)
        val span = shape.endX - shape.startX
        val relativeDistance = (distance / span).coerceIn(minimumValue = 0f, maximumValue = 1f)
        val falloff = 1f - wave.distanceFalloff * relativeDistance
        val weight = waveWeight(x = x, shape = shape, ramp = wave.ramp)
        return wave.amplitude * falloff * weight * seaSurface(x = x, wave = wave)
    }

    fun seaSurface(
        x: Float,
        wave: FluidWave,
    ): Float {
        var height = 0f
        for (index in SeaSwells.indices) {
            val swell = SeaSwells[index]
            val angle = swellAngle(x = x, wave = wave, swell = swell, index = index)
            height += swell.amplitude * (cos(x = angle) + CREST_SHARPNESS * cos(x = 2 * angle))
        }
        return height / SeaSwellNormalization
    }

    fun waveWeight(
        x: Float,
        shape: FluidShape,
        ramp: Float,
    ): Float {
        val leftCapWeight =
            smoothStep(
                edgeStart = shape.leftCapCenter,
                edgeEnd = shape.leftCapCenter + ramp,
                x = x,
            )
        val rightCapWeight =
            smoothStep(
                edgeStart = shape.rightCapCenter,
                edgeEnd = shape.rightCapCenter - ramp,
                x = x,
            )
        val distanceOutsideBlob = abs(x = x - shape.blobCenterX) - shape.blobRadiusXAt(x = x)
        val blobWeight = smoothStep(edgeStart = 0f, edgeEnd = ramp, x = distanceOutsideBlob)
        return leftCapWeight * rightCapWeight * blobWeight
    }

    private fun swellAngle(
        x: Float,
        wave: FluidWave,
        swell: SeaSwell,
        index: Int,
    ): Float {
        val spatial = TWO_PI * x / (wave.wavelength * swell.wavelengthRatio)
        val temporal = wave.phase * swell.dispersion
        val seedShift = wave.seed * (index + 1)
        return spatial - temporal + swell.offset + seedShift
    }

    private fun circleHalfHeight(
        distance: Float,
        radius: Float,
    ): Float = if (distance >= radius) 0f else sqrt(x = radius * radius - distance * distance)

    private fun smoothStep(
        edgeStart: Float,
        edgeEnd: Float,
        x: Float,
    ): Float {
        val normalizedX = (x - edgeStart) / (edgeEnd - edgeStart)
        val clampedX = normalizedX.coerceIn(minimumValue = 0f, maximumValue = 1f)
        return clampedX * clampedX * (3f - 2f * clampedX)
    }
}

internal fun Density.blobRadius(
    barHeight: Float,
    press: Float,
): Float {
    val baseRadius = barHeight / 2f + FluidBottomBarTokens.BlobOverflow.toPx()
    val clampedPress = press.coerceIn(minimumValue = 0f, maximumValue = 1f)
    val pressScale = 1f + clampedPress * FluidBottomBarTokens.PRESS_GROW

    return baseRadius * pressScale
}
