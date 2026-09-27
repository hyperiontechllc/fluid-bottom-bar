package dev.hyperiontech.fluidbar.render.mesh

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.util.lerp
import dev.hyperiontech.fluidbar.FluidPalette
import dev.hyperiontech.fluidbar.state.FluidBarState
import kotlin.math.sin

internal class MeshFrame(
    val previous: FluidPalette,
    val current: FluidPalette,
    val spread: Float,
    val bubbleOpacity: Float,
    val bandOpacity: Float,
    val bounds: Rect,
    val focus: Offset,
    val rippleCenter: Offset,
    val blobRadius: Float,
    val flow: MeshFlow,
)

internal class DriftWave(
    val frequency: Float,
    val offset: Float,
)

internal class MeshFlow(
    private val phase: Float,
    private val intensity: Float,
) {
    fun drift(wave: DriftWave): Float = sin(x = phase * wave.frequency + wave.offset) * intensity
}

internal fun meshFlowOf(state: FluidBarState): MeshFlow =
    MeshFlow(
        phase = state.wavePhase.value,
        intensity =
            lerp(
                start = MeshTokens.FLOW_REST_INTENSITY,
                stop = 1f,
                fraction = state.waveEnergy.value,
            ),
    )
