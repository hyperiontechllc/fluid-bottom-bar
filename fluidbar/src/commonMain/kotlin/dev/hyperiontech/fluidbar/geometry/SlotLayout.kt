package dev.hyperiontech.fluidbar.geometry

internal object SlotLayout {
    private const val SLOT_CENTER_OFFSET = 0.5f

    fun slotCenterX(
        position: Float,
        slotCount: Int,
        width: Float,
        horizontalInset: Float,
    ): Float {
        val slotWidth =
            slotWidth(
                slotCount = slotCount,
                width = width,
                horizontalInset = horizontalInset,
            )
        return horizontalInset + (position + SLOT_CENTER_OFFSET) * slotWidth
    }

    fun slotPositionAt(
        x: Float,
        slotCount: Int,
        width: Float,
        horizontalInset: Float,
    ): Float {
        val slotWidth =
            slotWidth(
                slotCount = slotCount,
                width = width,
                horizontalInset = horizontalInset,
            )
        return (x - horizontalInset) / slotWidth - SLOT_CENTER_OFFSET
    }

    private fun slotWidth(
        slotCount: Int,
        width: Float,
        horizontalInset: Float,
    ): Float {
        require(value = slotCount > 0) { "slotCount must be positive, was $slotCount" }
        return (width - 2 * horizontalInset) / slotCount
    }
}
