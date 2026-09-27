package dev.hyperiontech.fluidbar.render.mesh

import androidx.compose.ui.graphics.Brush
import dev.hyperiontech.fluidbar.FluidPalette
import dev.hyperiontech.fluidbar.render.unitGlow
import dev.hyperiontech.fluidbar.render.unitRadialGradient

internal class PaletteGlows(
    palette: FluidPalette,
    private val bubbleOpacity: Float,
    private val bandOpacity: Float,
) {
    val base: Brush =
        unitRadialGradient(
            colorStops =
                arrayOf(
                    0f to palette.fillMiddle.copy(alpha = bubbleAlpha(MeshTokens.BASE_CORE_ALPHA)),
                    MeshTokens.BASE_MIDDLE_STOP to
                        palette.fillOuter.copy(alpha = bandAlpha(MeshTokens.BASE_MIDDLE_ALPHA)),
                    1f to palette.fillEdge.copy(alpha = bandAlpha(MeshTokens.BASE_EDGE_ALPHA)),
                ),
        )
    val accent: Brush =
        unitGlow(color = palette.strokeMiddle.copy(alpha = bandAlpha(MeshTokens.ACCENT_ALPHA)))
    val startTint: Brush =
        unitGlow(color = palette.strokeStart.copy(alpha = bandAlpha(MeshTokens.END_TINT_ALPHA)))
    val endTint: Brush =
        unitGlow(color = palette.strokeEnd.copy(alpha = bandAlpha(MeshTokens.END_TINT_ALPHA)))
    val sweep: Brush =
        unitGlow(color = palette.fillOuter.copy(alpha = bandAlpha(MeshTokens.SWEEP_ALPHA)))
    val core: Brush =
        unitGlow(color = palette.fillCore.copy(alpha = bubbleAlpha(MeshTokens.CORE_ALPHA)))

    private fun bubbleAlpha(alpha: Float): Float = scaled(alpha = alpha, opacity = bubbleOpacity)

    private fun bandAlpha(alpha: Float): Float = scaled(alpha = alpha, opacity = bandOpacity)

    private fun scaled(
        alpha: Float,
        opacity: Float,
    ): Float = (alpha * opacity).coerceIn(minimumValue = 0f, maximumValue = 1f)
}

internal class PaletteGlowCache {
    private val glowsByPalette = mutableMapOf<FluidPalette, PaletteGlows>()
    private var cachedBubbleOpacity = Float.NaN
    private var cachedBandOpacity = Float.NaN

    fun glowsFor(
        palette: FluidPalette,
        bubbleOpacity: Float,
        bandOpacity: Float,
    ): PaletteGlows {
        if (bubbleOpacity != cachedBubbleOpacity || bandOpacity != cachedBandOpacity) {
            glowsByPalette.clear()
            cachedBubbleOpacity = bubbleOpacity
            cachedBandOpacity = bandOpacity
        }
        val glows =
            glowsByPalette.remove(key = palette)
                ?: PaletteGlows(
                    palette = palette,
                    bubbleOpacity = bubbleOpacity,
                    bandOpacity = bandOpacity,
                )
        glowsByPalette[palette] = glows
        if (glowsByPalette.size > MAX_CACHED_PALETTES) {
            glowsByPalette.remove(key = glowsByPalette.keys.first())
        }
        return glows
    }

    private companion object {
        const val MAX_CACHED_PALETTES = 3
    }
}
