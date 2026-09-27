package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.hyperiontech.fluidbar.geometry.FluidShape
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.math.max

internal class HaloMaskPainter {
    private val haloOpacity = Color.Black.copy(alpha = FluidBottomBarTokens.HALO_ALPHA)
    private var cachedCoreRatio = Float.NaN
    private var cachedBrush: Brush? = null

    fun DrawScope.drawHaloMask(
        shape: FluidShape,
        center: Offset,
        bounds: Rect,
    ) {
        val coreRadius = max(a = shape.blobReachX, b = shape.blobRadiusY)
        val haloRadius = coreRadius + FluidBottomBarTokens.HaloSpread.toPx()
        drawUnitRect(
            brush = brushFor(coreRatio = coreRadius / haloRadius),
            bounds = bounds,
            origin = center,
            scaleX = haloRadius,
            scaleY = haloRadius,
            blendMode = BlendMode.DstIn,
        )
    }

    private fun brushFor(coreRatio: Float): Brush {
        cachedBrush?.takeIf { coreRatio == cachedCoreRatio }?.let { return it }
        return unitRadialGradient(
            colorStops =
                arrayOf(
                    0f to haloOpacity,
                    coreRatio to haloOpacity,
                    1f to Color.Transparent,
                ),
        ).also { brush ->
            cachedBrush = brush
            cachedCoreRatio = coreRatio
        }
    }
}
