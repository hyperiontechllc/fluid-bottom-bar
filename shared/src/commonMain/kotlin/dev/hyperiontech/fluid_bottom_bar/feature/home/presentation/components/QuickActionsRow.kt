package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeDimens
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.QuickAction
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun QuickActionsRow(
    modifier: Modifier = Modifier,
    onQuickActionClick: (QuickAction) -> Unit,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        QuickAction.entries.forEach { action ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(size = HomeDimens.QuickActionSize),
                    shape = CircleShape,
                    border = cardBorder,
                    onClick = { onQuickActionClick(action) },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = vectorResource(resource = action.icon),
                            contentDescription = null,
                        )
                    }
                }
                Text(
                    text = stringResource(resource = action.label),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}
