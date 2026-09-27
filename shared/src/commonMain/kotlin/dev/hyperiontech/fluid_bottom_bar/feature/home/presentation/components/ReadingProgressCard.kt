package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.ReadingProgress
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeDimens
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.style
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.reading_minutes_left
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReadingProgressCard(
    modifier: Modifier = Modifier,
    progress: ReadingProgress,
) {
    Surface(
        modifier = modifier.width(width = HomeDimens.ProgressCardWidth),
        shape = MaterialTheme.shapes.large,
        border = cardBorder,
    ) {
        Column(
            modifier = Modifier.padding(all = HomeDimens.CardPadding),
            verticalArrangement = Arrangement.spacedBy(space = HomeDimens.CardPadding),
        ) {
            ArticleThumbnail(category = progress.article.category)
            Text(
                text = progress.article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { progress.completedFraction },
                color = progress.article.category.style.accent,
            )
            Text(
                text =
                    stringResource(
                        resource = Res.string.reading_minutes_left,
                        progress.minutesLeft,
                    ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
