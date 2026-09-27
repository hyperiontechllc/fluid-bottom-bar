package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeDimens

internal val cardBorder: BorderStroke
    @Composable
    @ReadOnlyComposable
    get() =
        BorderStroke(
            width = HomeDimens.CardBorderWidth,
            color = MaterialTheme.colorScheme.outline,
        )
