package dev.hyperiontech.fluidbar.geometry

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SlotLayoutTest {
    @Test
    fun slotCenterX_placesCentersEvenlyWithinInsets() {
        val centers = (0 until SLOT_COUNT).map { slotCenterX(position = it.toFloat()) }

        assertEquals(expected = listOf(62f, 162f, 262f, 362f), actual = centers)
    }

    @Test
    fun slotCenterX_interpolatesBetweenSlots() {
        assertEquals(expected = 212f, actual = slotCenterX(position = 1.5f))
    }

    @Test
    fun slotPositionAt_invertsSlotCenterX() {
        (0 until SLOT_COUNT).forEach { slot ->
            val position = slotPositionAt(x = slotCenterX(position = slot.toFloat()))

            assertEquals(
                expected = slot.toFloat(),
                actual = position,
                absoluteTolerance = TOLERANCE,
            )
        }
    }

    @Test
    fun slotPositionAt_extendsBeyondFirstAndLastSlot() {
        assertEquals(
            expected = -0.62f,
            actual = slotPositionAt(x = 0f),
            absoluteTolerance = TOLERANCE,
        )
        assertEquals(
            expected = 3.62f,
            actual = slotPositionAt(x = WIDTH),
            absoluteTolerance = TOLERANCE,
        )
    }

    @Test
    fun slotCenterX_rejectsEmptySlots() {
        assertFailsWith<IllegalArgumentException> {
            SlotLayout.slotCenterX(position = 0f, slotCount = 0, width = 100f, horizontalInset = 0f)
        }
    }

    @Test
    fun slotPositionAt_rejectsEmptySlots() {
        assertFailsWith<IllegalArgumentException> {
            SlotLayout.slotPositionAt(x = 0f, slotCount = 0, width = 100f, horizontalInset = 0f)
        }
    }

    private fun slotCenterX(position: Float): Float =
        SlotLayout.slotCenterX(
            position = position,
            slotCount = SLOT_COUNT,
            width = WIDTH,
            horizontalInset = HORIZONTAL_INSET,
        )

    private fun slotPositionAt(x: Float): Float =
        SlotLayout.slotPositionAt(
            x = x,
            slotCount = SLOT_COUNT,
            width = WIDTH,
            horizontalInset = HORIZONTAL_INSET,
        )

    private companion object {
        const val SLOT_COUNT = 4
        const val WIDTH = 424f
        const val HORIZONTAL_INSET = 12f
        const val TOLERANCE = 1e-4f
    }
}
