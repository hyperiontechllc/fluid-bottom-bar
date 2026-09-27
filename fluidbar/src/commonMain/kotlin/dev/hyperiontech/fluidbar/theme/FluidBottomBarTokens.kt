package dev.hyperiontech.fluidbar.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI

internal object FluidBottomBarTokens {
    val Height = 66.dp
    val MaxWidth = 364.dp
    val ContentHorizontalPadding = 12.dp
    val ShadowElevation = 16.dp

    val BarBorderWidth = 1.25.dp
    const val BAR_BORDER_TOP_ALPHA = 0.32f
    const val BAR_BORDER_BOTTOM_ALPHA = 0.10f

    val FluidOutset = 1.dp
    val BlobOverflow = 8.dp
    val NeckSmoothing = 14.dp
    val FluidLayerPadding = 6.dp
    const val FLUID_SAMPLE_COUNT = 160

    val WaveRestAmplitude = 2.5.dp
    val WaveExcitedAmplitude = 4.5.dp
    val WaveLength = 128.dp
    val WaveRamp = 18.dp
    const val WAVE_DISTANCE_FALLOFF = 0.6f
    const val BOTTOM_WAVE_AMPLITUDE_RATIO = 0.8f
    const val BOTTOM_WAVE_LENGTH_RATIO = 1.12f
    const val BOTTOM_WAVE_SEED = 2.3f
    const val WAVE_PHASE_TRAVEL = (4 * PI).toFloat()

    const val STRETCH_PER_SLOT_VELOCITY = 0.07f
    const val MAX_STRETCH = 0.32f
    const val STRETCH_HEIGHT_RATIO = 0.45f
    const val JELLY_KICK_VELOCITY = 2.4f
    const val MAX_JELLY = 0.22f
    const val JELLY_WIDTH_RATIO = 0.6f
    const val DRAG_FOLLOW_STIFFNESS = 900f
    const val DRAG_FOLLOW_DAMPING_RATIO = 0.8f
    const val PRESS_GROW = 0.08f
    const val PULL_PER_SLOT = 2f
    const val MAX_PULL = 0.14f

    val HaloSpread = 14.dp
    const val HALO_ALPHA = 0.45f

    val ColorSpreadFeather = 56.dp
    const val PALETTE_HANDOFF_THRESHOLD = 0.5f

    const val LIGHT_SURFACE_LUMINANCE = 0.5f
    const val LIGHT_SURFACE_BUBBLE_OPACITY = 2f
    const val LIGHT_SURFACE_BAND_OPACITY = 0.45f

    val IconSize = 20.dp
    const val PRESSED_SCALE = 0.9f

    private const val ICON_TINT_DURATION_MILLIS = 300
    private const val WAVE_DURATION_MILLIS = 2400
    private const val COLOR_SPREAD_DURATION_MILLIS = 1100
    private const val BOUNCE_DURATION_MILLIS = 340
    private const val BOUNCE_PEAK_MILLIS = BOUNCE_DURATION_MILLIS / 2
    private val EaseInOut = CubicBezierEasing(a = 0.4f, b = 0f, c = 0.6f, d = 1f)
    private val EaseOutQuint = CubicBezierEasing(a = 0.22f, b = 1f, c = 0.36f, d = 1f)

    val BlobSpec: AnimationSpec<Float> = spring(dampingRatio = 0.5f, stiffness = 240f)
    val BlobPressSpec: AnimationSpec<Float> = spring(dampingRatio = 0.55f, stiffness = 500f)
    val BlobReleaseSpec: AnimationSpec<Float> = spring(dampingRatio = 0.3f, stiffness = 350f)
    val JellySpec: AnimationSpec<Float> = spring(dampingRatio = 0.26f, stiffness = 380f)
    val WaveEnergySpec: AnimationSpec<Float> =
        tween(durationMillis = WAVE_DURATION_MILLIS, easing = EaseInOut)
    val WavePhaseSpec: AnimationSpec<Float> =
        tween(durationMillis = WAVE_DURATION_MILLIS, easing = LinearOutSlowInEasing)
    val ColorSpreadSpec: AnimationSpec<Float> =
        tween(durationMillis = COLOR_SPREAD_DURATION_MILLIS, easing = EaseOutQuint)
    val IconTintSpec: AnimationSpec<Color> = tween(durationMillis = ICON_TINT_DURATION_MILLIS)
    val PressSpec: AnimationSpec<Float> = spring(stiffness = Spring.StiffnessMedium)

    val IconBounceScaleSpec: AnimationSpec<Float> =
        keyframes {
            durationMillis = BOUNCE_DURATION_MILLIS
            0.85f at 0 using FastOutSlowInEasing
            1.15f at BOUNCE_PEAK_MILLIS using FastOutSlowInEasing
        }
    val IconBounceOffsetSpec: AnimationSpec<Float> =
        keyframes {
            durationMillis = BOUNCE_DURATION_MILLIS
            2f at 0 using FastOutSlowInEasing
            (-3f) at BOUNCE_PEAK_MILLIS using FastOutSlowInEasing
        }
}
