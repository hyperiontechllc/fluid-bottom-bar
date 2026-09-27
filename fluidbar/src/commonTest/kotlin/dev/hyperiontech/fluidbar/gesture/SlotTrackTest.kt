package dev.hyperiontech.fluidbar.gesture

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SlotTrackTest {
    private val track =
        SlotTrack(
            slotCount = SLOT_COUNT,
            width = WIDTH,
            horizontalInset = HORIZONTAL_INSET,
            pressedBlobRadius = 50f,
        )

    @Test
    fun positionAt_mapsSlotCentersToSlotPositions() {
        assertEquals(
            expected = 0f,
            actual = track.positionAt(x = 62f),
            absoluteTolerance = TOLERANCE,
        )
        assertEquals(
            expected = 2f,
            actual = track.positionAt(x = 262f),
            absoluteTolerance = TOLERANCE,
        )
    }

    @Test
    fun positionAt_clampsToFirstAndLastSlot() {
        assertEquals(expected = 0f, actual = track.positionAt(x = 0f))
        assertEquals(expected = LAST_SLOT, actual = track.positionAt(x = WIDTH))
    }

    @Test
    fun dragTargetAt_keepsTheGrabOffset() {
        val target = track.dragTargetAt(x = 162f, grabOffset = 0.25f)

        assertEquals(expected = 0.75f, actual = target, absoluteTolerance = TOLERANCE)
    }

    @Test
    fun isOnBlob_acceptsTouchesInsideTheDrawnBubble() {
        assertTrue(
            actual = track.isOnBlob(x = 162f, blobPosition = 1f, blobRadius = BLOB_RADIUS),
        )
        assertTrue(
            actual =
                track.isOnBlob(
                    x = 162f + BLOB_RADIUS,
                    blobPosition = 1f,
                    blobRadius = BLOB_RADIUS,
                ),
        )
        assertTrue(
            actual =
                track.isOnBlob(
                    x = 162f - BLOB_RADIUS,
                    blobPosition = 1f,
                    blobRadius = BLOB_RADIUS,
                ),
        )
    }

    @Test
    fun isOnBlob_rejectsTouchesJustOutsideTheBubbleEvenInsideItsSlot() {
        val justOutside = 162f + BLOB_RADIUS + 1f

        assertFalse(
            actual = track.isOnBlob(x = justOutside, blobPosition = 1f, blobRadius = BLOB_RADIUS),
        )
        assertEquals(
            expected = 1,
            actual = track.slotIndexAt(position = track.positionAt(x = justOutside)),
        )
    }

    @Test
    fun isOnBlob_followsTheBubbleBetweenSlots() {
        assertTrue(
            actual = track.isOnBlob(x = 212f, blobPosition = 1.5f, blobRadius = BLOB_RADIUS),
        )
        assertFalse(
            actual = track.isOnBlob(x = 162f, blobPosition = 2f, blobRadius = BLOB_RADIUS),
        )
    }

    @Test
    fun isOnBlob_mirrorsInRightToLeftLayout() {
        val rtlTrack = rtlTrack()

        assertTrue(
            actual = rtlTrack.isOnBlob(x = 362f, blobPosition = 0f, blobRadius = BLOB_RADIUS),
        )
        assertFalse(
            actual = rtlTrack.isOnBlob(x = 62f, blobPosition = 0f, blobRadius = BLOB_RADIUS),
        )
    }

    @Test
    fun grabOffsetAt_keepsOffsetWhenGrabbingOuterSideOfFirstSlot() {
        val grabOffset = track.grabOffsetAt(x = 32f, blobPosition = 0f)
        val target = track.dragTargetAt(x = 132f, grabOffset = grabOffset)

        assertEquals(expected = -0.3f, actual = grabOffset, absoluteTolerance = TOLERANCE)
        assertEquals(expected = 1f, actual = target, absoluteTolerance = TOLERANCE)
    }

    @Test
    fun grabOffsetAt_keepsOffsetWhenGrabbingOuterSideOfLastSlot() {
        val grabOffset = track.grabOffsetAt(x = 392f, blobPosition = LAST_SLOT)
        val target = track.dragTargetAt(x = 292f, grabOffset = grabOffset)

        assertEquals(expected = 0.3f, actual = grabOffset, absoluteTolerance = TOLERANCE)
        assertEquals(expected = LAST_SLOT - 1f, actual = target, absoluteTolerance = TOLERANCE)
    }

    @Test
    fun dragTargetAt_overshootsUntilBlobReachesBarEdge() {
        val atStart = track.dragTargetAt(x = 0f, grabOffset = 0f)
        val atEnd = track.dragTargetAt(x = WIDTH, grabOffset = 0f)

        assertEquals(
            expected = -EXPECTED_OVERSHOOT,
            actual = atStart,
            absoluteTolerance = TOLERANCE,
        )
        assertEquals(
            expected = LAST_SLOT + EXPECTED_OVERSHOOT,
            actual = atEnd,
            absoluteTolerance = TOLERANCE,
        )
    }

    @Test
    fun dragTargetAt_doesNotOvershootWhenBlobAlreadyTouchesBarEdge() {
        val wideBlobTrack =
            SlotTrack(
                slotCount = SLOT_COUNT,
                width = WIDTH,
                horizontalInset = HORIZONTAL_INSET,
                pressedBlobRadius = 70f,
            )

        val atStart = wideBlobTrack.dragTargetAt(x = 0f, grabOffset = 0f)
        val atEnd = wideBlobTrack.dragTargetAt(x = WIDTH, grabOffset = 0f)

        assertEquals(expected = 0f, actual = atStart, absoluteTolerance = TOLERANCE)
        assertEquals(expected = LAST_SLOT, actual = atEnd, absoluteTolerance = TOLERANCE)
    }

    @Test
    fun slotIndexAt_roundsToNearestSlot() {
        assertEquals(expected = 1, actual = track.slotIndexAt(position = 1.4f))
        assertEquals(expected = 2, actual = track.slotIndexAt(position = 1.6f))
    }

    @Test
    fun slotIndexAt_clampsOvershootToEndSlots() {
        assertEquals(expected = 0, actual = track.slotIndexAt(position = -EXPECTED_OVERSHOOT))
        assertEquals(
            expected = SLOT_COUNT - 1,
            actual = track.slotIndexAt(position = LAST_SLOT + 0.6f),
        )
    }

    @Test
    fun positionAt_mirrorsSlotsInRightToLeftLayout() {
        val rtlTrack = rtlTrack()

        assertEquals(
            expected = LAST_SLOT,
            actual = rtlTrack.positionAt(x = 62f),
            absoluteTolerance = TOLERANCE,
        )
        assertEquals(
            expected = 0f,
            actual = rtlTrack.positionAt(x = 362f),
            absoluteTolerance = TOLERANCE,
        )
    }

    @Test
    fun dragTargetAt_movesTowardsHigherSlotsWhenDraggingLeftInRightToLeftLayout() {
        val rtlTrack = rtlTrack()

        val nearStart = rtlTrack.dragTargetAt(x = 300f, grabOffset = 0f)
        val furtherLeft = rtlTrack.dragTargetAt(x = 200f, grabOffset = 0f)

        assertEquals(
            expected = 1f,
            actual = furtherLeft - nearStart,
            absoluteTolerance = TOLERANCE,
        )
    }

    private fun rtlTrack(): SlotTrack =
        SlotTrack(
            slotCount = SLOT_COUNT,
            width = WIDTH,
            horizontalInset = HORIZONTAL_INSET,
            pressedBlobRadius = 50f,
            isRtl = true,
        )

    private companion object {
        const val SLOT_COUNT = 4
        const val LAST_SLOT = 3f
        const val WIDTH = 424f
        const val HORIZONTAL_INSET = 12f
        const val EXPECTED_OVERSHOOT = 0.12f
        const val TOLERANCE = 1e-4f
        const val BLOB_RADIUS = 41f
    }
}
