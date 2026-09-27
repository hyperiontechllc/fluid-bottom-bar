package dev.hyperiontech.fluidbar.gesture

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.requireLayoutDirection
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.LayoutDirection
import dev.hyperiontech.fluidbar.geometry.blobRadius
import dev.hyperiontech.fluidbar.state.FluidBarState
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlinx.coroutines.launch
import kotlin.math.abs

internal fun Modifier.bubbleDrag(
    state: FluidBarState,
    slotCount: Int,
    onDrop: (Int) -> Unit,
): Modifier = this then BubbleDragElement(state = state, slotCount = slotCount, onDrop = onDrop)

private data class BubbleDragElement(
    val state: FluidBarState,
    val slotCount: Int,
    val onDrop: (Int) -> Unit,
) : ModifierNodeElement<BubbleDragNode>() {
    override fun create(): BubbleDragNode =
        BubbleDragNode(
            state = state,
            slotCount = slotCount,
            onDrop = onDrop,
        )

    override fun update(node: BubbleDragNode) {
        node.update(state = state, slotCount = slotCount, onDrop = onDrop)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "bubbleDrag"
        properties["slotCount"] = slotCount
    }
}

private class BubbleDragNode(
    private var state: FluidBarState,
    private var slotCount: Int,
    private var onDrop: (Int) -> Unit,
) : DelegatingNode() {
    private val pointerInputNode =
        delegate(delegatableNode = SuspendingPointerInputModifierNode { detectBubbleGestures() })

    fun update(
        state: FluidBarState,
        slotCount: Int,
        onDrop: (Int) -> Unit,
    ) {
        this.onDrop = onDrop
        if (state == this.state && slotCount == this.slotCount) return
        this.state = state
        this.slotCount = slotCount
        pointerInputNode.resetPointerInputHandler()
    }

    private suspend fun PointerInputScope.detectBubbleGestures() {
        awaitEachGesture {
            val track =
                SlotTrack(
                    slotCount = slotCount,
                    width = size.width.toFloat(),
                    horizontalInset = FluidBottomBarTokens.ContentHorizontalPadding.toPx(),
                    pressedBlobRadius = blobRadius(barHeight = size.height.toFloat(), press = 1f),
                    isRtl = requireLayoutDirection() == LayoutDirection.Rtl,
                )
            val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
            val isOnBlob =
                track.isOnBlob(
                    x = down.position.x,
                    blobPosition = state.blobPosition.value,
                    blobRadius =
                        blobRadius(
                            barHeight = size.height.toFloat(),
                            press = state.press.value,
                        ),
                )
            if (!isOnBlob) return@awaitEachGesture

            down.consume()
            state.pointerDown(position = track.positionAt(x = down.position.x))
            launchAnimation { state.pressDown() }
            val slopChange = awaitHorizontalSlop(down = down)
            if (slopChange != null) dragBlob(down = down, slopChange = slopChange, track = track)
            launchAnimation { state.pressUp() }
        }
    }

    private suspend fun AwaitPointerEventScope.awaitHorizontalSlop(down: PointerInputChange): PointerInputChange? {
        while (true) {
            val change = awaitBlobChange(pointerId = down.id) ?: return null
            if (!change.pressed) return null
            if (abs(x = change.position.x - down.position.x) > viewConfiguration.touchSlop) {
                return change
            }
        }
    }

    private suspend fun AwaitPointerEventScope.dragBlob(
        down: PointerInputChange,
        slopChange: PointerInputChange,
        track: SlotTrack,
    ) {
        val grabOffset =
            track.grabOffsetAt(x = down.position.x, blobPosition = state.blobPosition.value)
        state.startDrag()
        state.dragTarget = track.dragTargetAt(x = slopChange.position.x, grabOffset = grabOffset)
        val follower = launchAnimation { state.followPointer() }
        var dropIndex: Int? = null
        try {
            val lifted =
                followUntilLifted(
                    pointerId = down.id,
                    track = track,
                    grabOffset = grabOffset,
                )
            if (lifted) dropIndex = track.slotIndexAt(position = state.dragTarget)
        } finally {
            follower.cancel()
            state.endDrag()
            val releaseIndex = dropIndex ?: state.targetIndex
            launchAnimation { state.releaseDrag(toIndex = releaseIndex) }
        }
        dropIndex?.let { index -> onDrop(index) }
    }

    private suspend fun AwaitPointerEventScope.followUntilLifted(
        pointerId: PointerId,
        track: SlotTrack,
        grabOffset: Float,
    ): Boolean {
        while (true) {
            val change = awaitBlobChange(pointerId = pointerId) ?: return false
            if (!change.pressed) return true
            state.dragTarget = track.dragTargetAt(x = change.position.x, grabOffset = grabOffset)
        }
    }

    private suspend fun AwaitPointerEventScope.awaitBlobChange(pointerId: PointerId): PointerInputChange? {
        val change =
            awaitPointerEvent(pass = PointerEventPass.Initial)
                .changes
                .firstOrNull { it.id == pointerId }
        change?.consume()
        return change
    }

    private fun launchAnimation(block: suspend () -> Unit) = coroutineScope.launch { block() }
}
