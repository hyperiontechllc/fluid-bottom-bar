package dev.hyperiontech.fluidbar.state

import dev.hyperiontech.fluidbar.FluidPalettes
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FluidBarStateTest {
    private val state =
        FluidBarState(
            initialIndex = INITIAL_INDEX,
            initialPalette = FluidPalettes.Aurora,
        )

    @Test
    fun initialState_restsOnInitialIndex() {
        assertEquals(expected = INITIAL_INDEX, actual = state.targetIndex)
        assertEquals(expected = INITIAL_INDEX.toFloat(), actual = state.blobPosition.value)
        assertEquals(expected = INITIAL_INDEX.toFloat(), actual = state.pointerPosition)
        assertEquals(expected = FluidPalettes.Aurora, actual = state.currentPalette)
        assertEquals(expected = FluidPalettes.Aurora, actual = state.previousPalette)
        assertEquals(expected = 0f, actual = state.press.value)
        assertFalse(actual = state.isDragging)
    }

    @Test
    fun pull_isZeroWhenPointerIsOnBlob() {
        state.pointerDown(position = INITIAL_INDEX.toFloat())

        assertEquals(expected = 0f, actual = state.pull)
    }

    @Test
    fun pull_pointsTowardsThePointer() {
        val offset = 0.25f

        state.pointerDown(position = INITIAL_INDEX + offset)
        assertEquals(expected = offset * FluidBottomBarTokens.PULL_PER_SLOT, actual = state.pull)

        state.movePointer(position = INITIAL_INDEX - offset)
        assertEquals(expected = -offset * FluidBottomBarTokens.PULL_PER_SLOT, actual = state.pull)
    }

    @Test
    fun pull_isClampedToUnitRange() {
        state.pointerDown(position = INITIAL_INDEX + 3f)
        assertEquals(expected = 1f, actual = state.pull)

        state.movePointer(position = INITIAL_INDEX - 3f)
        assertEquals(expected = -1f, actual = state.pull)
    }

    @Test
    fun startDrag_beginsFromTheBlobPosition() {
        state.pointerDown(position = INITIAL_INDEX + 0.2f)
        state.startDrag()

        assertTrue(actual = state.isDragging)
        assertEquals(expected = state.blobPosition.value, actual = state.dragTarget)
    }

    @Test
    fun startDrag_locksPullUntilNextPointerDown() {
        state.pointerDown(position = INITIAL_INDEX + 0.2f)
        val pullAtDragStart = state.pull
        state.startDrag()

        state.movePointer(position = INITIAL_INDEX - 1f)
        state.endDrag()
        assertEquals(expected = pullAtDragStart, actual = state.pull)

        state.pointerDown(position = INITIAL_INDEX.toFloat())
        assertEquals(expected = 0f, actual = state.pull)
    }

    @Test
    fun endDrag_stopsDragging() {
        state.startDrag()
        state.endDrag()

        assertFalse(actual = state.isDragging)
    }

    private companion object {
        const val INITIAL_INDEX = 1
    }
}
