package dev.hyperiontech.fluidbar.render

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.requireGraphicsContext
import androidx.compose.ui.platform.InspectorInfo
import dev.hyperiontech.fluidbar.FluidBottomBarColors
import dev.hyperiontech.fluidbar.state.FluidBarState
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens

internal fun Modifier.fluidBarBackground(
    state: FluidBarState,
    slotCount: Int,
    colors: FluidBottomBarColors,
): Modifier =
    this then
        FluidBarBackgroundElement(
            state = state,
            slotCount = slotCount,
            colors = colors,
        )

private data class FluidBarBackgroundElement(
    val state: FluidBarState,
    val slotCount: Int,
    val colors: FluidBottomBarColors,
) : ModifierNodeElement<FluidBarBackgroundNode>() {
    override fun create(): FluidBarBackgroundNode =
        FluidBarBackgroundNode(
            state = state,
            slotCount = slotCount,
            colors = colors,
        )

    override fun update(node: FluidBarBackgroundNode) {
        node.state = state
        node.slotCount = slotCount
        node.colors = colors
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "fluidBarBackground"
        properties["slotCount"] = slotCount
    }
}

private class FluidBarBackgroundNode(
    var state: FluidBarState,
    var slotCount: Int,
    colors: FluidBottomBarColors,
) : Modifier.Node(),
    DrawModifierNode {
    private lateinit var renderer: FluidBarRenderer
    private var emphasis = SurfaceEmphasis.of(colors = colors)

    var colors: FluidBottomBarColors = colors
        set(value) {
            if (value == field) return
            field = value
            emphasis = SurfaceEmphasis.of(colors = value)
        }

    override fun onAttach() {
        renderer = FluidBarRenderer(meshLayer = requireGraphicsContext().createGraphicsLayer())
    }

    override fun onDetach() {
        requireGraphicsContext().releaseGraphicsLayer(layer = renderer.meshLayer)
    }

    override fun ContentDrawScope.draw() {
        with(receiver = renderer) {
            drawFluidBar(state = state, slotCount = slotCount, colors = colors, emphasis = emphasis)
        }
        drawContent()
    }
}

internal data class SurfaceEmphasis(
    val bubbleOpacity: Float,
    val bandOpacity: Float,
) {
    companion object {
        val Dark = SurfaceEmphasis(bubbleOpacity = 1f, bandOpacity = 1f)
        val Light =
            SurfaceEmphasis(
                bubbleOpacity = FluidBottomBarTokens.LIGHT_SURFACE_BUBBLE_OPACITY,
                bandOpacity = FluidBottomBarTokens.LIGHT_SURFACE_BAND_OPACITY,
            )

        fun of(colors: FluidBottomBarColors): SurfaceEmphasis {
            val surface =
                lerp(start = colors.surfaceTop, stop = colors.surfaceBottom, fraction = 0.5f)
            val isLightSurface = surface.luminance() > FluidBottomBarTokens.LIGHT_SURFACE_LUMINANCE
            return if (isLightSurface) Light else Dark
        }
    }
}
