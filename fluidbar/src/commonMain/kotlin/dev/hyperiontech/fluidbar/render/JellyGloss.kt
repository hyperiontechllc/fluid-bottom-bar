package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.hyperiontech.fluidbar.geometry.FluidShape
import kotlin.math.max

private object GlossTokens {
    const val SHEEN_ALPHA = 0.16f
    const val SHEEN_END_STOP = 0.45f
    val SpecularOffset = Offset(x = -0.28f, y = -0.52f)
    const val SPECULAR_RADIUS_SCALE = 0.42f
    const val SPECULAR_ALPHA = 0.5f
    const val DEPTH_OFFSET_Y = 0.95f
    const val DEPTH_RADIUS_SCALE = 1.1f
    const val DEPTH_ALPHA = 0.28f
}

internal class JellyGlossPainter {
    private val sheenBrush =
        Brush.verticalGradient(
            colorStops =
                arrayOf(
                    0f to Color.White.copy(alpha = GlossTokens.SHEEN_ALPHA),
                    GlossTokens.SHEEN_END_STOP to Color.Transparent,
                ),
            startY = 0f,
            endY = 1f,
        )
    private val specularBrush =
        unitGlow(color = Color.White.copy(alpha = GlossTokens.SPECULAR_ALPHA))
    private val depthBrush = unitGlow(color = Color.Black.copy(alpha = GlossTokens.DEPTH_ALPHA))

    fun DrawScope.drawJellyGloss(
        shape: FluidShape,
        centerY: Float,
        bounds: Rect,
    ) {
        drawSheen(shape = shape, centerY = centerY, bounds = bounds)
        val blobCenter = Offset(x = shape.blobCenterX, y = centerY)
        val blobRadius = Offset(x = shape.blobRadiusX, y = shape.blobRadiusY)
        drawUnitGlow(
            brush = specularBrush,
            center = blobCenter + GlossTokens.SpecularOffset.scaledBy(scale = blobRadius),
            radius = shape.blobRadiusX * GlossTokens.SPECULAR_RADIUS_SCALE,
            blendMode = BlendMode.SrcAtop,
        )
        drawUnitGlow(
            brush = depthBrush,
            center =
                blobCenter +
                    Offset(
                        x = 0f,
                        y = shape.blobRadiusY * GlossTokens.DEPTH_OFFSET_Y,
                    ),
            radius = shape.blobRadiusX * GlossTokens.DEPTH_RADIUS_SCALE,
            blendMode = BlendMode.SrcAtop,
        )
    }

    private fun DrawScope.drawSheen(
        shape: FluidShape,
        centerY: Float,
        bounds: Rect,
    ) {
        val sheenTop = centerY - max(a = shape.bandHalfHeight, b = shape.blobRadiusY)
        drawUnitRect(
            brush = sheenBrush,
            bounds = bounds,
            origin = Offset(x = 0f, y = sheenTop),
            scaleX = 1f,
            scaleY = centerY - sheenTop,
            blendMode = BlendMode.SrcAtop,
        )
    }
}

private fun Offset.scaledBy(scale: Offset) = Offset(x = x * scale.x, y = y * scale.y)
