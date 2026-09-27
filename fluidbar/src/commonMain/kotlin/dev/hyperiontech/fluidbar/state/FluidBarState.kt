package dev.hyperiontech.fluidbar.state

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import dev.hyperiontech.fluidbar.FluidPalette
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.sqrt

@Stable
internal class FluidBarState(
    initialIndex: Int,
    initialPalette: FluidPalette,
) {
    var targetIndex by mutableIntStateOf(value = initialIndex)
        private set
    var currentPalette by mutableStateOf(value = initialPalette)
        private set
    var previousPalette by mutableStateOf(value = initialPalette)
        private set

    val blobPosition = Animatable(initialValue = initialIndex.toFloat())
    val jelly = Animatable(initialValue = 0f)
    val waveEnergy = Animatable(initialValue = 0f)
    val wavePhase = Animatable(initialValue = 0f)
    val colorSpread = Animatable(initialValue = 1f)
    val press = Animatable(initialValue = 0f)
    var pointerPosition by mutableFloatStateOf(value = initialIndex.toFloat())
        private set
    var dragTarget = initialIndex.toFloat()

    var isDragging by mutableStateOf(value = false)
        private set
    private var dragVelocity = 0f
    private var dragPull = 0f
    private var isPullLocked by mutableStateOf(value = false)
    val pull: Float
        get() = if (isPullLocked) dragPull else livePull()

    fun pointerDown(position: Float) {
        pointerPosition = position
        isPullLocked = false
    }

    fun movePointer(position: Float) {
        pointerPosition = position
    }

    fun startDrag() {
        dragVelocity = blobPosition.velocity
        dragPull = pull
        dragTarget = blobPosition.value
        isPullLocked = true
        isDragging = true
    }

    fun endDrag() {
        isDragging = false
    }

    suspend fun pressDown() {
        press.animateTo(targetValue = 1f, animationSpec = FluidBottomBarTokens.BlobPressSpec)
    }

    suspend fun pressUp() {
        press.animateTo(targetValue = 0f, animationSpec = FluidBottomBarTokens.BlobReleaseSpec)
    }

    suspend fun followPointer() {
        var lastFrameNanos = withFrameNanos { it }
        while (true) {
            val frameNanos = withFrameNanos { it }
            val elapsedSeconds = (frameNanos - lastFrameNanos) / NANOS_PER_SECOND
            lastFrameNanos = frameNanos
            stepTowardsDragTarget(dt = elapsedSeconds.coerceAtMost(maximumValue = MAX_FOLLOW_STEP))
        }
    }

    suspend fun releaseDrag(toIndex: Int) {
        blobPosition.animateTo(
            targetValue = toIndex.toFloat(),
            animationSpec = FluidBottomBarTokens.BlobSpec,
            initialVelocity = dragVelocity,
        )
    }

    suspend fun returnToSelectionIfRejected(droppedIndex: Int) {
        val isConfirmed =
            withTimeoutOrNull(timeMillis = SELECTION_CONFIRMATION_TIMEOUT_MILLIS) {
                snapshotFlow { targetIndex }.first { index -> index == droppedIndex }
            } != null
        if (!isConfirmed && !isDragging) returnToSelection()
    }

    suspend fun returnToSelection() {
        blobPosition.animateTo(
            targetValue = targetIndex.toFloat(),
            animationSpec = FluidBottomBarTokens.BlobSpec,
        )
    }

    suspend fun settleOn(
        index: Int,
        palette: FluidPalette,
    ) {
        if (index == targetIndex && palette == currentPalette) return
        val waveDirection = if (index >= targetIndex) 1f else -1f
        handOffPalette(palette = palette)
        targetIndex = index

        coroutineScope {
            launch { spreadColor() }
            launch { moveBlobTo(index = index) }
            launch { kickJelly() }
            launch { exciteWaves() }
            launch { travelWaves(direction = waveDirection) }
        }
    }

    private fun livePull(): Float {
        val offset = (pointerPosition - blobPosition.value) * FluidBottomBarTokens.PULL_PER_SLOT
        return offset.coerceIn(minimumValue = -1f, maximumValue = 1f)
    }

    private suspend fun stepTowardsDragTarget(dt: Float) {
        val stiffness = FluidBottomBarTokens.DRAG_FOLLOW_STIFFNESS
        val damping = 2f * FluidBottomBarTokens.DRAG_FOLLOW_DAMPING_RATIO * sqrt(x = stiffness)
        val displacement = dragTarget - blobPosition.value
        val acceleration = displacement * stiffness - dragVelocity * damping
        dragVelocity += acceleration * dt
        blobPosition.snapTo(targetValue = blobPosition.value + dragVelocity * dt)
    }

    private fun handOffPalette(palette: FluidPalette) {
        val isSpreadMostlyDone = colorSpread.value >= FluidBottomBarTokens.PALETTE_HANDOFF_THRESHOLD
        if (isSpreadMostlyDone) previousPalette = currentPalette
        currentPalette = palette
    }

    private suspend fun spreadColor() {
        colorSpread.snapTo(targetValue = 0f)
        colorSpread.animateTo(
            targetValue = 1f,
            animationSpec = FluidBottomBarTokens.ColorSpreadSpec,
        )
    }

    private suspend fun moveBlobTo(index: Int) {
        blobPosition.animateTo(
            targetValue = index.toFloat(),
            animationSpec = FluidBottomBarTokens.BlobSpec,
        )
    }

    private suspend fun kickJelly() {
        jelly.animateTo(
            targetValue = 0f,
            animationSpec = FluidBottomBarTokens.JellySpec,
            initialVelocity = FluidBottomBarTokens.JELLY_KICK_VELOCITY,
        )
    }

    private suspend fun exciteWaves() {
        waveEnergy.snapTo(targetValue = 1f)
        waveEnergy.animateTo(targetValue = 0f, animationSpec = FluidBottomBarTokens.WaveEnergySpec)
    }

    private suspend fun travelWaves(direction: Float) {
        wavePhase.animateTo(
            targetValue = wavePhase.value + direction * FluidBottomBarTokens.WAVE_PHASE_TRAVEL,
            animationSpec = FluidBottomBarTokens.WavePhaseSpec,
        )
    }

    private companion object {
        const val MAX_FOLLOW_STEP = 1f / 30f
        const val NANOS_PER_SECOND = 1_000_000_000f
        const val SELECTION_CONFIRMATION_TIMEOUT_MILLIS = 250L
    }
}
