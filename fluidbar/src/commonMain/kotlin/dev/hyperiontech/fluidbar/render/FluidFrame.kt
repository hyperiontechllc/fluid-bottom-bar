package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.util.lerp
import dev.hyperiontech.fluidbar.geometry.FluidShape
import dev.hyperiontech.fluidbar.geometry.FluidWave
import dev.hyperiontech.fluidbar.geometry.SlotLayout
import dev.hyperiontech.fluidbar.geometry.blobRadius
import dev.hyperiontech.fluidbar.state.FluidBarState
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

internal fun DrawScope.fluidShapeOf(
    state: FluidBarState,
    slotCount: Int,
): FluidShape {
    val barHalfHeight: Float = size.height / 2
    val outset: Float = FluidBottomBarTokens.FluidOutset.toPx()
    val press: Float = state.press.value
    val blobRadius = blobRadius(barHeight = size.height, press = press)
    val stretch =
        if (state.isDragging) {
            0f
        } else {
            val velocityStretch =
                abs(x = state.blobPosition.velocity) *
                    FluidBottomBarTokens.STRETCH_PER_SLOT_VELOCITY

            velocityStretch.coerceAtMost(maximumValue = FluidBottomBarTokens.MAX_STRETCH)
        }
    val jelly =
        state.jelly.value.coerceIn(
            minimumValue = -FluidBottomBarTokens.MAX_JELLY,
            maximumValue = FluidBottomBarTokens.MAX_JELLY,
        )
    return FluidShape(
        left = -outset,
        right = size.width + outset,
        bandHalfHeight = barHalfHeight + outset,
        blobCenterX = slotCenterX(position = state.blobPosition.value, slotCount = slotCount),
        blobRadiusX =
            blobRadius *
                (1f + stretch - jelly * FluidBottomBarTokens.JELLY_WIDTH_RATIO),
        blobRadiusY =
            blobRadius *
                (1f - stretch * FluidBottomBarTokens.STRETCH_HEIGHT_RATIO + jelly),
        blobLean = state.pull * press * FluidBottomBarTokens.MAX_PULL,
        neckSmoothing = FluidBottomBarTokens.NeckSmoothing.toPx(),
    )
}

internal fun DrawScope.topWaveOf(state: FluidBarState): FluidWave =
    FluidWave(
        amplitude =
            lerp(
                start = FluidBottomBarTokens.WaveRestAmplitude.toPx(),
                stop = FluidBottomBarTokens.WaveExcitedAmplitude.toPx(),
                fraction = state.waveEnergy.value,
            ),
        wavelength = FluidBottomBarTokens.WaveLength.toPx(),
        phase = state.wavePhase.value,
        ramp = FluidBottomBarTokens.WaveRamp.toPx(),
        distanceFalloff = FluidBottomBarTokens.WAVE_DISTANCE_FALLOFF,
        seed = 0f,
    )

internal fun FluidWave.toBottomWave(): FluidWave =
    copy(
        amplitude = amplitude * FluidBottomBarTokens.BOTTOM_WAVE_AMPLITUDE_RATIO,
        wavelength = wavelength * FluidBottomBarTokens.BOTTOM_WAVE_LENGTH_RATIO,
        seed = FluidBottomBarTokens.BOTTOM_WAVE_SEED,
    )

internal fun DrawScope.slotCenterX(
    position: Float,
    slotCount: Int,
): Float =
    SlotLayout.slotCenterX(
        position = position,
        slotCount = slotCount,
        width = size.width,
        horizontalInset = FluidBottomBarTokens.ContentHorizontalPadding.toPx(),
    )

internal fun DrawScope.fluidLayerBounds(
    shape: FluidShape,
    centerY: Float,
    amplitude: Float,
): Rect {
    val padding = FluidBottomBarTokens.FluidLayerPadding.toPx()
    val haloReach =
        max(
            a = shape.blobReachX,
            b = shape.blobRadiusY,
        ) + FluidBottomBarTokens.HaloSpread.toPx()
    val halfHeight =
        max(
            a = max(a = size.height / 2, b = shape.blobRadiusY) + amplitude,
            b = haloReach,
        ) + padding
    return Rect(
        left =
            floor(
                x =
                    minOf(
                        a = 0f,
                        b = shape.startX,
                        c = shape.blobCenterX - haloReach,
                    ) - padding,
            ),
        top = floor(x = centerY - halfHeight),
        right =
            ceil(
                x =
                    maxOf(
                        a = size.width,
                        b = shape.endX,
                        c = shape.blobCenterX + haloReach,
                    ) + padding,
            ),
        bottom = ceil(x = centerY + halfHeight),
    )
}
