package dev.hyperiontech.fluidbar.state

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Stable
import dev.hyperiontech.fluidbar.theme.FluidBottomBarTokens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Stable
internal class IconBounceState {
    val scale = Animatable(initialValue = 1f)
    val offsetY = Animatable(initialValue = 0f)

    suspend fun play() =
        coroutineScope {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = FluidBottomBarTokens.IconBounceScaleSpec,
                )
            }
            launch {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = FluidBottomBarTokens.IconBounceOffsetSpec,
                )
            }
        }
}
