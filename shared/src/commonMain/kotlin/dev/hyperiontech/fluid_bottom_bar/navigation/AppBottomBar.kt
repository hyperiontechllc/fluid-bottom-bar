package dev.hyperiontech.fluid_bottom_bar.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.hyperiontech.fluid_bottom_bar.ui.theme.FluidTheme
import dev.hyperiontech.fluidbar.FluidBarItem
import dev.hyperiontech.fluidbar.FluidBottomBar
import dev.hyperiontech.fluidbar.FluidBottomBarDefaults
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.enums.EnumEntries

private val BarHorizontalMargin = 20.dp
private val BarBottomMargin = 12.dp

@Composable
fun AppBottomBar(
    selectedDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val destinations: EnumEntries<AppDestination> = AppDestination.entries
    val isDarkTheme = FluidTheme.colors.isDark
    val items =
        destinations.map { destination ->
            FluidBarItem(
                icon = painterResource(resource = destination.icon),
                label = stringResource(resource = destination.label),
                palette = destination.palette(isDarkTheme = isDarkTheme),
            )
        }
    val barColors =
        remember(key1 = isDarkTheme) {
            if (isDarkTheme) {
                FluidBottomBarDefaults.darkColors()
            } else {
                FluidBottomBarDefaults.lightColors()
            }
        }
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = BarHorizontalMargin)
                .padding(bottom = BarBottomMargin),
        contentAlignment = Alignment.Center,
    ) {
        FluidBottomBar(
            items = items,
            selectedIndex = selectedDestination.ordinal,
            onItemSelected = { index -> onDestinationSelected(destinations[index]) },
            colors = barColors,
        )
    }
}
