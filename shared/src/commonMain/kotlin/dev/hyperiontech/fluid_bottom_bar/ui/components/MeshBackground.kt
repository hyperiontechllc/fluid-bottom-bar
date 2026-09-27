package dev.hyperiontech.fluid_bottom_bar.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.MeshGradientPainter
import androidx.compose.ui.graphics.painter.Painter
import dev.hyperiontech.fluid_bottom_bar.ui.theme.FluidColors
import dev.hyperiontech.fluid_bottom_bar.ui.theme.FluidTheme

private object MeshBackgroundTokens {
    const val TOP_START_ALPHA = 0.45f
    const val TOP_CENTER_ALPHA = 0.35f
    const val TOP_END_ALPHA = 0.30f
    const val MIDDLE_START_ALPHA = 0.20f
    const val MIDDLE_END_ALPHA = 0.15f

    val MiddleCenterPosition = Offset(x = 0.45f, y = 0.4f)
}

@Composable
internal fun rememberMeshBackgroundPainter(): Painter {
    val intensity = FluidTheme.colors.meshIntensity
    return remember(key1 = intensity) {
        val topStart =
            FluidColors.Indigo600.copy(
                alpha = MeshBackgroundTokens.TOP_START_ALPHA * intensity,
            )
        val topCenter =
            FluidColors.Violet600.copy(
                alpha = MeshBackgroundTokens.TOP_CENTER_ALPHA * intensity,
            )
        val topEnd =
            FluidColors.Fuchsia600.copy(
                alpha = MeshBackgroundTokens.TOP_END_ALPHA * intensity,
            )
        val middleStart =
            FluidColors.Blue600.copy(
                alpha = MeshBackgroundTokens.MIDDLE_START_ALPHA * intensity,
            )
        val middleEnd =
            FluidColors.Pink600.copy(
                alpha = MeshBackgroundTokens.MIDDLE_END_ALPHA * intensity,
            )
        MeshGradientPainter(rows = 2, columns = 2) {
            setVertex(
                row = 0,
                column = 0,
                position = Offset(x = 0f, y = 0f),
                color = topStart,
            )
            setVertex(
                row = 0,
                column = 1,
                position = Offset(x = 0.5f, y = 0f),
                color = topCenter,
            )
            setVertex(
                row = 0,
                column = 2,
                position = Offset(x = 1f, y = 0f),
                color = topEnd,
            )

            setVertex(
                row = 1,
                column = 0,
                position = Offset(x = 0f, y = 0.5f),
                color = middleStart,
            )
            setVertex(
                row = 1,
                column = 1,
                position = MeshBackgroundTokens.MiddleCenterPosition,
                color = Color.Transparent,
            )
            setVertex(
                row = 1,
                column = 2,
                position = Offset(x = 1f, y = 0.5f),
                color = middleEnd,
            )

            setVertex(
                row = 2,
                column = 0,
                position = Offset(x = 0f, y = 1f),
                color = Color.Transparent,
            )
            setVertex(
                row = 2,
                column = 1,
                position = Offset(x = 0.5f, y = 1f),
                color = Color.Transparent,
            )
            setVertex(
                row = 2,
                column = 2,
                position = Offset(x = 1f, y = 1f),
                color = Color.Transparent,
            )
        }
    }
}
