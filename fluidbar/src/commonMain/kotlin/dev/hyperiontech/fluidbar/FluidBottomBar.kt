package dev.hyperiontech.fluidbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import dev.hyperiontech.fluidbar.gesture.bubbleDrag
import dev.hyperiontech.fluidbar.render.fluidBarBackground
import dev.hyperiontech.fluidbar.state.FluidBarState
import dev.hyperiontech.fluidbar.state.IconBounceState
import dev.hyperiontech.fluidbar.tab.FluidBarTab
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlinx.coroutines.launch

@Composable
public fun FluidBottomBar(
    items: List<FluidBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: FluidBottomBarColors = FluidBottomBarDefaults.colors(),
) {
    require(value = items.isNotEmpty()) { "FluidBottomBar requires at least one item" }
    require(value = selectedIndex in items.indices) {
        "selectedIndex $selectedIndex is out of bounds for ${items.size} items"
    }

    val slotCount = items.size
    val selectedPalette = items[selectedIndex].palette
    val state =
        remember { FluidBarState(initialIndex = selectedIndex, initialPalette = selectedPalette) }
    val iconBounces = remember(key1 = slotCount) { List(size = slotCount) { IconBounceState() } }
    val coroutineScope = rememberCoroutineScope()
    val selectItem: (Int) -> Unit = { index ->
        if (index != selectedIndex) coroutineScope.launch { iconBounces[index].play() }
        onItemSelected(index)
    }
    LaunchedEffect(key1 = state, key2 = selectedIndex, key3 = selectedPalette) {
        state.settleOn(index = selectedIndex, palette = selectedPalette)
    }

    Row(
        modifier =
            modifier
                .widthIn(max = FluidBottomBarTokens.MaxWidth)
                .fillMaxWidth()
                .height(height = FluidBottomBarTokens.Height)
                .shadow(
                    elevation = FluidBottomBarTokens.ShadowElevation,
                    shape = CircleShape,
                    clip = false,
                    ambientColor = colors.shadow,
                    spotColor = colors.shadow,
                ).fluidBarBackground(state = state, slotCount = slotCount, colors = colors)
                .bubbleDrag(state = state, slotCount = slotCount) { index ->
                    if (index != selectedIndex) selectItem(index)
                    coroutineScope.launch {
                        state.returnToSelectionIfRejected(droppedIndex = index)
                    }
                }.padding(horizontal = FluidBottomBarTokens.ContentHorizontalPadding)
                .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            FluidBarTab(
                item = item,
                selected = index == selectedIndex,
                selectedIconColor = colors.selectedIcon,
                unselectedIconColor = colors.unselectedIcon,
                iconBounce = iconBounces[index],
                onClick = { selectItem(index) },
                modifier =
                    Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight(),
            )
        }
    }
}
