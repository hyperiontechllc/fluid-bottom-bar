package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.HomeFeed
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components.ArticleCard
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components.GreetingHeader
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components.QuickActionsRow
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.components.ReadingProgressCard
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.home_continue_reading
import fluidbottombar.shared.generated.resources.home_curated
import fluidbottombar.shared.generated.resources.home_quick_actions
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    contentPadding: PaddingValues,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onQuickActionClick: (QuickAction) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        contentPadding = contentPadding,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onQuickActionClick = onQuickActionClick,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    contentPadding: PaddingValues,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onQuickActionClick: (QuickAction) -> Unit,
) {
    when (uiState) {
        HomeUiState.Loading -> {
            Box(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(paddingValues = contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Success -> {
            HomeFeedContent(
                modifier = modifier,
                feed = uiState.feed,
                contentPadding = contentPadding,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onQuickActionClick = onQuickActionClick,
            )
        }
    }
}

@Composable
private fun HomeFeedContent(
    modifier: Modifier = Modifier,
    feed: HomeFeed,
    contentPadding: PaddingValues,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onQuickActionClick: (QuickAction) -> Unit,
) {
    val horizontalPadding = Modifier.padding(horizontal = HomeDimens.ScreenPadding)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(
                top = contentPadding.calculateTopPadding() + HomeDimens.ScreenPadding,
                bottom = contentPadding.calculateBottomPadding() + HomeDimens.ScreenPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(space = HomeDimens.ItemSpacing),
    ) {
        item(key = "greeting") {
            GreetingHeader(
                modifier = horizontalPadding.fillMaxWidth(),
                readerName = feed.readerName,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
            )
        }
        item(key = "featured") {
            ArticleCard(modifier = horizontalPadding.fillMaxWidth(), article = feed.featured)
        }
        item(key = "quick_actions_header") {
            SectionTitle(modifier = horizontalPadding, title = Res.string.home_quick_actions)
        }
        item(key = "quick_actions") {
            QuickActionsRow(
                modifier = horizontalPadding.fillMaxWidth(),
                onQuickActionClick = onQuickActionClick,
            )
        }
        if (feed.inProgress.isNotEmpty()) {
            item(key = "continue_reading_header") {
                SectionTitle(modifier = horizontalPadding, title = Res.string.home_continue_reading)
            }
            item(key = "continue_reading") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = HomeDimens.ScreenPadding),
                    horizontalArrangement = Arrangement.spacedBy(space = HomeDimens.ItemSpacing),
                ) {
                    items(items = feed.inProgress, key = { it.article.id }) { progress ->
                        ReadingProgressCard(progress = progress)
                    }
                }
            }
        }
        if (feed.curated.isNotEmpty()) {
            item(key = "curated_header") {
                SectionTitle(modifier = horizontalPadding, title = Res.string.home_curated)
            }
            items(items = feed.curated, key = { it.id }) { article ->
                ArticleCard(modifier = horizontalPadding.fillMaxWidth(), article = article)
            }
        }
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    title: StringResource,
) {
    Text(
        modifier = modifier.padding(top = HomeDimens.ItemSpacing).semantics { heading() },
        text = stringResource(resource = title),
        style = MaterialTheme.typography.titleMedium,
    )
}
