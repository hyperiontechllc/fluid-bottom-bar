package dev.hyperiontech.fluid_bottom_bar.feature.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.hyperiontech.fluid_bottom_bar.ui.theme.FluidTheme
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.placeholder_coming_soon
import org.jetbrains.compose.resources.stringResource

private val TitleSpacing = 6.dp

@Composable
fun DestinationPlaceholderScreen(
    modifier: Modifier = Modifier,
    title: String,
    contentPadding: PaddingValues,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(paddingValues = contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = TitleSpacing,
                alignment = Alignment.CenterVertically,
            ),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = FluidTheme.colors.textPrimary,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(resource = Res.string.placeholder_coming_soon),
            style = MaterialTheme.typography.bodyMedium,
            color = FluidTheme.colors.textTertiary,
        )
    }
}
