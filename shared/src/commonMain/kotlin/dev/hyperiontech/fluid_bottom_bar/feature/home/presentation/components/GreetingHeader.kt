package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.home_greeting
import fluidbottombar.shared.generated.resources.ic_moon
import fluidbottombar.shared.generated.resources.ic_sun
import fluidbottombar.shared.generated.resources.theme_switch_to_dark
import fluidbottombar.shared.generated.resources.theme_switch_to_light
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun GreetingHeader(
    modifier: Modifier = Modifier,
    readerName: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = stringResource(resource = Res.string.home_greeting),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                modifier = Modifier.semantics { heading() },
                text = readerName,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        IconButton(onClick = onToggleTheme) {
            Icon(
                imageVector =
                    vectorResource(
                        resource =
                            if (isDarkTheme) {
                                Res.drawable.ic_sun
                            } else {
                                Res.drawable.ic_moon
                            },
                    ),
                contentDescription =
                    stringResource(
                        resource =
                            if (isDarkTheme) {
                                Res.string.theme_switch_to_light
                            } else {
                                Res.string.theme_switch_to_dark
                            },
                    ),
            )
        }
    }
}
