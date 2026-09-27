package dev.hyperiontech.fluidbar.gesture

import dev.hyperiontech.fluidbar.geometry.SlotLayout
import kotlin.math.abs
import kotlin.math.roundToInt

internal class SlotTrack(
    private val slotCount: Int,
    private val width: Float,
    private val horizontalInset: Float,
    pressedBlobRadius: Float,
    private val isRtl: Boolean = false,
) {
    private val lastSlot = (slotCount - 1).toFloat()
    private val maxOvershoot = overshootUntilBarEdge(blobRadius = pressedBlobRadius)

    fun positionAt(x: Float): Float =
        unclampedPositionAt(x = leftToRightX(x = x)).coerceIn(
            minimumValue = 0f,
            maximumValue = lastSlot,
        )

    fun isOnBlob(
        x: Float,
        blobPosition: Float,
        blobRadius: Float,
    ): Boolean {
        val blobCenterX =
            SlotLayout.slotCenterX(
                position = blobPosition,
                slotCount = slotCount,
                width = width,
                horizontalInset = horizontalInset,
            )
        return abs(x = leftToRightX(x = x) - blobCenterX) <= blobRadius
    }

    fun grabOffsetAt(
        x: Float,
        blobPosition: Float,
    ): Float = unclampedPositionAt(x = leftToRightX(x = x)) - blobPosition

    fun dragTargetAt(
        x: Float,
        grabOffset: Float,
    ): Float {
        val target = unclampedPositionAt(x = leftToRightX(x = x)) - grabOffset
        return target.coerceIn(
            minimumValue = -maxOvershoot,
            maximumValue = lastSlot + maxOvershoot,
        )
    }

    fun slotIndexAt(position: Float): Int =
        position.roundToInt().coerceIn(
            minimumValue = 0,
            maximumValue = slotCount - 1,
        )

    private fun leftToRightX(x: Float): Float = if (isRtl) width - x else x

    private fun unclampedPositionAt(x: Float): Float =
        SlotLayout.slotPositionAt(
            x = x,
            slotCount = slotCount,
            width = width,
            horizontalInset = horizontalInset,
        )

    private fun overshootUntilBarEdge(blobRadius: Float): Float {
        val barStart = unclampedPositionAt(x = 0f)
        val blobRadiusInSlots = unclampedPositionAt(x = blobRadius) - barStart
        val blobCenterAtBarEdge = barStart + blobRadiusInSlots
        return (-blobCenterAtBarEdge).coerceAtLeast(minimumValue = 0f)
    }
}
