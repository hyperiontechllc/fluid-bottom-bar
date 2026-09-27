package dev.hyperiontech.fluid_bottom_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.hyperiontech.fluid_bottom_bar.feature.home.data.SampleHomeFeedRepository
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeRoute
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.HomeViewModel
import dev.hyperiontech.fluid_bottom_bar.feature.home.presentation.QuickAction
import dev.hyperiontech.fluid_bottom_bar.feature.placeholder.DestinationPlaceholderScreen
import dev.hyperiontech.fluid_bottom_bar.navigation.AppBottomBar
import dev.hyperiontech.fluid_bottom_bar.navigation.AppDestination
import dev.hyperiontech.fluid_bottom_bar.ui.components.rememberMeshBackgroundPainter
import dev.hyperiontech.fluid_bottom_bar.ui.theme.FluidTheme
import dev.hyperiontech.fluid_bottom_bar.ui.theme.SystemBarsAppearance
import dev.hyperiontech.fluidbar.FluidBottomBarContent
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    val isSystemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by rememberSaveable { mutableStateOf(value = isSystemDarkTheme) }
    SystemBarsAppearance(isDarkTheme = isDarkTheme)

    FluidTheme(isDarkTheme = isDarkTheme) {
        var selectedDestination by rememberSaveable { mutableStateOf(value = AppDestination.Home) }

        Scaffold(
            modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .paint(
                        painter = rememberMeshBackgroundPainter(),
                        contentScale = ContentScale.FillBounds,
                    ),
            containerColor = Color.Transparent,
            contentColor = FluidTheme.colors.textPrimary,
            bottomBar = {
                AppBottomBar(
                    selectedDestination = selectedDestination,
                    onDestinationSelected = { selectedDestination = it },
                )
            },
        ) { innerPadding ->
            FluidBottomBarContent(
                selectedIndex = selectedDestination.ordinal,
                pageCount = AppDestination.entries.size,
            ) { index ->
                DestinationContent(
                    destination = AppDestination.entries[index],
                    contentPadding = innerPadding,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    onNavigate = { selectedDestination = it },
                )
            }
        }
    }
}

@Composable
private fun DestinationContent(
    destination: AppDestination,
    contentPadding: PaddingValues,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNavigate: (AppDestination) -> Unit,
) {
    when (destination) {
        AppDestination.Home -> {
            HomeRoute(
                viewModel =
                    viewModel {
                        HomeViewModel(homeFeedRepository = SampleHomeFeedRepository())
                    },
                contentPadding = contentPadding,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onQuickActionClick = { action -> action.destination?.let(block = onNavigate) },
            )
        }

        AppDestination.Search,
        AppDestination.Saved,
        AppDestination.Profile,
        -> {
            DestinationPlaceholderScreen(
                title = stringResource(resource = destination.label),
                contentPadding = contentPadding,
            )
        }
    }
}

private val QuickAction.destination: AppDestination?
    get() =
        when (this) {
            QuickAction.NewPost -> null
            QuickAction.Search -> AppDestination.Search
            QuickAction.Saved -> AppDestination.Saved
            QuickAction.Profile -> AppDestination.Profile
        }
