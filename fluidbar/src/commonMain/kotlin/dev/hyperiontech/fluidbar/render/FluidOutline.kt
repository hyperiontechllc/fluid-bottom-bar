package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import dev.hyperiontech.fluidbar.geometry.FluidGeometry
import dev.hyperiontech.fluidbar.geometry.FluidShape
import dev.hyperiontech.fluidbar.geometry.FluidWave
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens

internal fun Path.buildFluidOutline(
    shape: FluidShape,
    centerY: Float,
    topWave: FluidWave,
    bottomWave: FluidWave,
) {
    val samples = FluidBottomBarTokens.FLUID_SAMPLE_COUNT
    val pointCount = 2 * samples

    fun pointAt(index: Int): Offset {
        val isTopEdge = index <= samples
        val fraction = (if (isTopEdge) index else pointCount - index).toFloat() / samples
        val x =
            FluidGeometry.cosineSampleX(
                fraction = fraction,
                start = shape.startX,
                end = shape.endX,
            )
        val halfHeight =
            FluidGeometry.edgeHalfHeight(
                x = x,
                shape = shape,
                wave = if (isTopEdge) topWave else bottomWave,
            )
        return Offset(x = x, y = if (isTopEdge) centerY - halfHeight else centerY + halfHeight)
    }

    rewind()
    val start = pointAt(index = 0)
    moveTo(x = start.x, y = start.y)
    var control = pointAt(index = 1)
    for (index in 1 until pointCount) {
        val next = pointAt(index = index + 1)
        quadraticTo(
            x1 = control.x,
            y1 = control.y,
            x2 = (control.x + next.x) / 2,
            y2 = (control.y + next.y) / 2,
        )
        control = next
    }
    close()
}
