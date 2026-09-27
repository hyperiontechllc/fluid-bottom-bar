package dev.hyperiontech.fluidbar.render.mesh

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.hyperiontech.fluidbar.render.drawUnitRect
import dev.hyperiontech.fluidbar.render.unitRadialGradient
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.math.hypot

internal class ColorSpreadMaskPainter {
    private var cachedSolidStop = Float.NaN
    private var cachedBrush: Brush? = null

    fun DrawScope.drawMask(
        mesh: MeshFrame,
        blendMode: BlendMode,
    ) {
        val feather = FluidBottomBarTokens.ColorSpreadFeather.toPx()
        val farthestCorner =
            farthestCornerDistance(center = mesh.rippleCenter, bounds = mesh.bounds)
        val radius = (mesh.spread * (farthestCorner + feather)).coerceAtLeast(minimumValue = 1f)
        val solidStop = ((radius - feather) / radius).coerceAtLeast(minimumValue = 0f)
        drawUnitRect(
            brush = brushFor(solidStop = solidStop),
            bounds = mesh.bounds,
            origin = mesh.rippleCenter,
            scaleX = radius,
            scaleY = radius,
            blendMode = blendMode,
        )
    }

    private fun brushFor(solidStop: Float): Brush {
        cachedBrush?.takeIf { solidStop == cachedSolidStop }?.let { return it }
        return unitRadialGradient(
            colorStops =
                arrayOf(
                    0f to Color.Black,
                    solidStop to Color.Black,
                    1f to Color.Transparent,
                ),
        ).also { brush ->
            cachedBrush = brush
            cachedSolidStop = solidStop
        }
    }

    private fun farthestCornerDistance(
        center: Offset,
        bounds: Rect,
    ): Float {
        val left = center.x - bounds.left
        val right = center.x - bounds.right
        val top = center.y - bounds.top
        val bottom = center.y - bounds.bottom
        return maxOf(
            a = maxOf(a = hypot(x = left, y = top), b = hypot(x = right, y = top)),
            b = maxOf(a = hypot(x = left, y = bottom), b = hypot(x = right, y = bottom)),
        )
    }
}
