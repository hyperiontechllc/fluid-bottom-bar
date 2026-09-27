package dev.hyperiontech.fluidbar.render.mesh

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.hyperiontech.fluidbar.render.withLayer

internal class CrossfadedMeshPainter {
    private val layerPaint = Paint()
    private val additivePaint = Paint().apply { blendMode = BlendMode.Plus }
    private val glowCache = PaletteGlowCache()
    private val spreadMask = ColorSpreadMaskPainter()

    fun DrawScope.drawCrossfadedMesh(mesh: MeshFrame) {
        val currentGlows =
            glowCache.glowsFor(
                palette = mesh.current,
                bubbleOpacity = mesh.bubbleOpacity,
                bandOpacity = mesh.bandOpacity,
            )
        if (mesh.spread >= 1f) {
            drawMesh(glows = currentGlows, mesh = mesh)
            return
        }
        val previousGlows =
            glowCache.glowsFor(
                palette = mesh.previous,
                bubbleOpacity = mesh.bubbleOpacity,
                bandOpacity = mesh.bandOpacity,
            )
        withLayer(bounds = mesh.bounds, paint = layerPaint) {
            drawMesh(glows = previousGlows, mesh = mesh)
            with(receiver = spreadMask) { drawMask(mesh = mesh, blendMode = BlendMode.DstOut) }
        }
        withLayer(bounds = mesh.bounds, paint = additivePaint) {
            drawMesh(glows = currentGlows, mesh = mesh)
            with(receiver = spreadMask) { drawMask(mesh = mesh, blendMode = BlendMode.DstIn) }
        }
    }
}
