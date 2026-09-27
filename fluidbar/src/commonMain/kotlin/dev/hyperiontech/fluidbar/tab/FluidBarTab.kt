package dev.hyperiontech.fluidbar.tab

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.hyperiontech.fluidbar.FluidBarItem
import dev.hyperiontech.fluidbar.state.IconBounceState
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens

@Composable
internal fun FluidBarTab(
    item: FluidBarItem,
    selected: Boolean,
    selectedIconColor: Color,
    unselectedIconColor: Color,
    iconBounce: IconBounceState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val targetScale = if (isPressed) FluidBottomBarTokens.PRESSED_SCALE else 1f
    val pressScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = FluidBottomBarTokens.PressSpec,
    )
    val targetTint = if (selected) selectedIconColor else unselectedIconColor
    val iconTint by animateColorAsState(
        targetValue = targetTint,
        animationSpec = FluidBottomBarTokens.IconTintSpec,
    )

    Box(
        modifier =
            modifier.selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(size = FluidBottomBarTokens.IconSize)
                    .semantics { contentDescription = item.label }
                    .graphicsLayer {
                        val scale = pressScale * iconBounce.scale.value
                        scaleX = scale
                        scaleY = scale
                        translationY =
                            iconBounce.offsetY.value.dp
                                .toPx()
                    }.drawBehind {
                        with(receiver = item.icon) {
                            draw(size = size, colorFilter = ColorFilter.tint(color = iconTint))
                        }
                    },
        )
    }
}
