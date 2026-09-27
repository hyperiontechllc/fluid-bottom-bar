package dev.hyperiontech.fluidbar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
public data class FluidBarItem(
    public val icon: Painter,
    public val label: String,
    public val palette: FluidPalette,
)
