package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.Article
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeDimens
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.style
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.article_meta
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, border = cardBorder) {
        Row(
            modifier = Modifier.padding(all = HomeDimens.CardPadding),
            horizontalArrangement = Arrangement.spacedBy(space = HomeDimens.ItemSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArticleThumbnail(category = article.category)
            Column {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text =
                        stringResource(
                            resource = Res.string.article_meta,
                            stringResource(resource = article.category.style.label),
                            article.readTimeMinutes,
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
