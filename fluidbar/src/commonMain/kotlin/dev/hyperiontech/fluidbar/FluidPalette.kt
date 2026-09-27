package dev.hyperiontech.fluidbar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.fluidbar.theme.FluidColorTokens

@Immutable
public data class FluidPalette(
    public val fillCore: Color,
    public val fillMiddle: Color,
    public val fillOuter: Color,
    public val fillEdge: Color,
    public val strokeStart: Color,
    public val strokeMiddle: Color,
    public val strokeEnd: Color,
)

public object FluidPalettes {
    public val Aurora: FluidPalette = FluidPalette(
        fillCore = FluidColorTokens.Sky400,
        fillMiddle = FluidColorTokens.Indigo600,
        fillOuter = FluidColorTokens.Purple600,
        fillEdge = FluidColorTokens.Blue600,
        strokeStart = FluidColorTokens.Sky200,
        strokeMiddle = FluidColorTokens.Violet300,
        strokeEnd = FluidColorTokens.Pink400,
    )

    public val Orchid: FluidPalette = FluidPalette(
        fillCore = FluidColorTokens.Indigo400,
        fillMiddle = FluidColorTokens.Purple500,
        fillOuter = FluidColorTokens.Pink500,
        fillEdge = FluidColorTokens.Blue500,
        strokeStart = FluidColorTokens.Indigo200,
        strokeMiddle = FluidColorTokens.Pink400,
        strokeEnd = FluidColorTokens.Orange400,
    )

    public val Sunset: FluidPalette = FluidPalette(
        fillCore = FluidColorTokens.Rose500,
        fillMiddle = FluidColorTokens.Pink500,
        fillOuter = FluidColorTokens.Orange500,
        fillEdge = FluidColorTokens.Purple500,
        strokeStart = FluidColorTokens.Rose200,
        strokeMiddle = FluidColorTokens.Orange300,
        strokeEnd = FluidColorTokens.Amber300,
    )

    public val Ember: FluidPalette = FluidPalette(
        fillCore = FluidColorTokens.Orange500,
        fillMiddle = FluidColorTokens.Red500,
        fillOuter = FluidColorTokens.Amber500,
        fillEdge = FluidColorTokens.Pink500,
        strokeStart = FluidColorTokens.Orange200,
        strokeMiddle = FluidColorTokens.Amber300,
        strokeEnd = FluidColorTokens.Pink400,
    )
}
