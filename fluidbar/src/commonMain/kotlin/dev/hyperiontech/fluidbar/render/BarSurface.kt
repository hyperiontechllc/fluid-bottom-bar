package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import dev.hyperiontech.fluidbar.FluidBottomBarColors
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens

internal class BarSurfacePainter {
    private var surfaceColors: FluidBottomBarColors? = null
    private var surfaceBrush: Brush? = null
    private var borderColor = Color.Unspecified
    private var borderBrush: Brush? = null
    private var borderStroke: Stroke? = null

    fun DrawScope.drawBarSurface(
        colors: FluidBottomBarColors,
        border: Color,
    ) {
        val cornerRadius = CornerRadius(x = size.height / 2)
        val borderWidth = FluidBottomBarTokens.BarBorderWidth.toPx()
        drawRoundRect(brush = surfaceBrushFor(colors = colors), cornerRadius = cornerRadius)
        drawRoundRect(
            brush = borderBrushFor(color = border),
            topLeft = Offset(x = borderWidth / 2, y = borderWidth / 2),
            size = size.copy(width = size.width - borderWidth, height = size.height - borderWidth),
            cornerRadius = cornerRadius,
            style = strokeFor(width = borderWidth),
        )
    }

    private fun surfaceBrushFor(colors: FluidBottomBarColors): Brush {
        surfaceBrush?.takeIf { colors == surfaceColors }?.let { return it }
        return Brush
            .verticalGradient(colors = listOf(colors.surfaceTop, colors.surfaceBottom))
            .also { brush ->
                surfaceBrush = brush
                surfaceColors = colors
            }
    }

    private fun borderBrushFor(color: Color): Brush {
        borderBrush?.takeIf { color == borderColor }?.let { return it }
        return Brush
            .verticalGradient(
                colors =
                    listOf(
                        color.copy(alpha = FluidBottomBarTokens.BAR_BORDER_TOP_ALPHA),
                        color.copy(alpha = FluidBottomBarTokens.BAR_BORDER_BOTTOM_ALPHA),
                    ),
            ).also { brush ->
                borderBrush = brush
                borderColor = color
            }
    }

    private fun strokeFor(width: Float): Stroke =
        borderStroke?.takeIf { it.width == width } ?: Stroke(width = width).also {
            borderStroke = it
        }
}
