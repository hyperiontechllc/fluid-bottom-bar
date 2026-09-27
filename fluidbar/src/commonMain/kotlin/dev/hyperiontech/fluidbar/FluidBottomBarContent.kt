package dev.hyperiontech.fluidbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.zIndex

private const val PAGE_FADE_MILLIS = 220

@Composable
public fun FluidBottomBarContent(
    selectedIndex: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable (index: Int) -> Unit,
) {
    require(value = selectedIndex in 0 until pageCount) {
        "selectedIndex $selectedIndex is out of bounds for $pageCount pages"
    }

    val stateHolder = rememberSaveableStateHolder()
    val composedPages = remember { mutableStateListOf(selectedIndex) }
    val pages =
        if (selectedIndex in composedPages) {
            composedPages
        } else {
            composedPages + selectedIndex
        }

    LaunchedEffect(key1 = selectedIndex) {
        if (selectedIndex !in composedPages) composedPages += selectedIndex
    }
    LaunchedEffect(key1 = pageCount) {
        repeat(times = pageCount) { index ->
            withFrameNanos { }
            if (index !in composedPages) composedPages += index
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        pages.forEach { index ->
            if (index < pageCount) {
                key(index) {
                    RetainedPage(isSelected = index == selectedIndex) {
                        stateHolder.SaveableStateProvider(key = index) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RetainedPage(
    isSelected: Boolean,
    content: @Composable () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = PAGE_FADE_MILLIS),
    )
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .zIndex(zIndex = if (isSelected) 1f else 0f)
                .placedWhile { alpha > 0f }
                .graphicsLayer { this.alpha = alpha }
                .claimPointerHits()
                .then(other = if (isSelected) Modifier else Modifier.inactivePage()),
    ) {
        content()
    }
}

private fun Modifier.claimPointerHits(): Modifier =
    pointerInput(key1 = Unit) {
        awaitPointerEventScope {
            while (true) awaitPointerEvent()
        }
    }

private fun Modifier.inactivePage(): Modifier = clearAndSetSemantics { }

private fun Modifier.placedWhile(isVisible: () -> Boolean): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints = constraints)
        layout(width = placeable.width, height = placeable.height) {
            if (isVisible()) placeable.place(x = 0, y = 0)
        }
    }
