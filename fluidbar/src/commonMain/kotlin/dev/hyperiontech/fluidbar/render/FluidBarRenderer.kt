package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import dev.hyperiontech.fluidbar.FluidBottomBarColors
import dev.hyperiontech.fluidbar.render.mesh.CrossfadedMeshPainter
import dev.hyperiontech.fluidbar.render.mesh.MeshFrame
import dev.hyperiontech.fluidbar.render.mesh.meshFlowOf
import dev.hyperiontech.fluidbar.state.FluidBarState

internal class FluidBarRenderer(
    val meshLayer: GraphicsLayer,
) {
    private val fluidPath = Path()
    private val layerPaint = Paint()
    private val meshPaint = Paint().apply { blendMode = BlendMode.SrcIn }
    private val surfacePainter = BarSurfacePainter()
    private val meshPainter = CrossfadedMeshPainter()
    private val haloPainter = HaloMaskPainter()
    private val glossPainter = JellyGlossPainter()

    fun DrawScope.drawFluidBar(
        state: FluidBarState,
        slotCount: Int,
        colors: FluidBottomBarColors,
        emphasis: SurfaceEmphasis,
    ) {
        if (layoutDirection == LayoutDirection.Rtl) {
            scale(scaleX = -1f, scaleY = 1f) {
                drawLeftToRight(
                    state = state,
                    slotCount = slotCount,
                    colors = colors,
                    emphasis = emphasis,
                )
            }
        } else {
            drawLeftToRight(
                state = state,
                slotCount = slotCount,
                colors = colors,
                emphasis = emphasis,
            )
        }
    }

    private fun DrawScope.drawLeftToRight(
        state: FluidBarState,
        slotCount: Int,
        colors: FluidBottomBarColors,
        emphasis: SurfaceEmphasis,
    ) {
        val shape = fluidShapeOf(state = state, slotCount = slotCount)
        val centerY = size.height / 2
        val border =
            lerp(
                start = state.previousPalette.fillCore,
                stop = state.currentPalette.fillCore,
                fraction = state.colorSpread.value,
            )
        with(receiver = surfacePainter) { drawBarSurface(colors = colors, border = border) }

        val topWave = topWaveOf(state = state)
        fluidPath.buildFluidOutline(
            shape = shape,
            centerY = centerY,
            topWave = topWave,
            bottomWave = topWave.toBottomWave(),
        )

        val bounds =
            fluidLayerBounds(shape = shape, centerY = centerY, amplitude = topWave.amplitude)
        val focus = Offset(x = shape.blobCenterX, y = centerY)
        val rippleCenterX =
            slotCenterX(position = state.targetIndex.toFloat(), slotCount = slotCount)
        val mesh =
            MeshFrame(
                previous = state.previousPalette,
                current = state.currentPalette,
                spread = state.colorSpread.value,
                bubbleOpacity = emphasis.bubbleOpacity,
                bandOpacity = emphasis.bandOpacity,
                bounds = bounds,
                focus = focus,
                rippleCenter = Offset(x = rippleCenterX, y = centerY),
                blobRadius = shape.blobRadiusY,
                flow = meshFlowOf(state = state),
            )

        recordMesh(mesh = mesh)
        withLayer(bounds = bounds, paint = layerPaint) {
            drawLayer(graphicsLayer = meshLayer)
            with(receiver = haloPainter) {
                drawHaloMask(
                    shape = shape,
                    center = focus,
                    bounds = bounds,
                )
            }
        }
        withLayer(bounds = bounds, paint = layerPaint) {
            drawPath(path = fluidPath, color = Color.White)
            withLayer(bounds = bounds, paint = meshPaint) {
                drawLayer(graphicsLayer = meshLayer)
            }
            with(receiver = glossPainter) {
                drawJellyGloss(
                    shape = shape,
                    centerY = centerY,
                    bounds = bounds,
                )
            }
        }
    }

    private fun DrawScope.recordMesh(mesh: MeshFrame) {
        val bounds = mesh.bounds
        meshLayer.topLeft = IntOffset(x = bounds.left.toInt(), y = bounds.top.toInt())
        meshLayer.record(
            density = this,
            layoutDirection = layoutDirection,
            size = IntSize(width = bounds.width.toInt(), height = bounds.height.toInt()),
        ) {
            translate(left = -bounds.left, top = -bounds.top) {
                with(receiver = meshPainter) { drawCrossfadedMesh(mesh = mesh) }
            }
        }
    }
}
