package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform

internal fun unitRadialGradient(vararg colorStops: Pair<Float, Color>): Brush =
    Brush.radialGradient(colorStops = colorStops, center = Offset.Zero, radius = 1f)

internal fun unitGlow(color: Color): Brush =
    unitRadialGradient(
        colorStops =
            arrayOf(
                0f to color,
                1f to Color.Transparent,
            ),
    )

internal inline fun DrawScope.inUnitSpace(
    origin: Offset,
    scaleX: Float,
    scaleY: Float,
    block: DrawScope.() -> Unit,
) {
    withTransform(
        transformBlock = {
            translate(left = origin.x, top = origin.y)
            scale(scaleX = scaleX, scaleY = scaleY, pivot = Offset.Zero)
        },
        drawBlock = block,
    )
}

internal fun DrawScope.drawUnitGlow(
    brush: Brush,
    center: Offset,
    radius: Float,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) {
    inUnitSpace(origin = center, scaleX = radius, scaleY = radius) {
        drawCircle(brush = brush, radius = 1f, center = Offset.Zero, blendMode = blendMode)
    }
}

internal fun DrawScope.drawUnitRect(
    brush: Brush,
    bounds: Rect,
    origin: Offset,
    scaleX: Float,
    scaleY: Float,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) {
    inUnitSpace(origin = origin, scaleX = scaleX, scaleY = scaleY) {
        drawRect(
            brush = brush,
            topLeft =
                Offset(
                    x = (bounds.left - origin.x) / scaleX,
                    y = (bounds.top - origin.y) / scaleY,
                ),
            size = Size(width = bounds.width / scaleX, height = bounds.height / scaleY),
            blendMode = blendMode,
        )
    }
}

internal inline fun DrawScope.withLayer(
    bounds: Rect,
    paint: Paint,
    crossinline block: DrawScope.() -> Unit,
) {
    drawContext.canvas.saveLayer(bounds = bounds, paint = paint)
    block()
    drawContext.canvas.restore()
}
