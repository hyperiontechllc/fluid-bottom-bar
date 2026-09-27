package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.ArticleCategory
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeDimens
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.style
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun ArticleThumbnail(
    modifier: Modifier = Modifier,
    category: ArticleCategory,
) {
    Box(
        modifier =
            modifier
                .size(size = HomeDimens.ThumbnailSize)
                .background(
                    brush = Brush.linearGradient(colors = category.style.artwork),
                    shape = MaterialTheme.shapes.medium,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = vectorResource(resource = category.style.icon),
            contentDescription = null,
            tint = Color.White,
        )
    }
}
