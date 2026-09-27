package dev.hyperiontech.fluid_bottom_bar.navigation

import dev.hyperiontech.fluid_bottom_bar.ui.theme.LightBarPalettes
import dev.hyperiontech.fluidbar.FluidPalette
import dev.hyperiontech.fluidbar.FluidPalettes
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.ic_bookmark
import fluidbottombar.shared.generated.resources.ic_home
import fluidbottombar.shared.generated.resources.ic_person
import fluidbottombar.shared.generated.resources.ic_search
import fluidbottombar.shared.generated.resources.nav_home
import fluidbottombar.shared.generated.resources.nav_profile
import fluidbottombar.shared.generated.resources.nav_saved
import fluidbottombar.shared.generated.resources.nav_search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class AppDestination(
    val label: StringResource,
) {
    Home(label = Res.string.nav_home),
    Search(label = Res.string.nav_search),
    Saved(label = Res.string.nav_saved),
    Profile(label = Res.string.nav_profile),
}

internal val AppDestination.icon: DrawableResource
    get() =
        when (this) {
            AppDestination.Home -> Res.drawable.ic_home
            AppDestination.Search -> Res.drawable.ic_search
            AppDestination.Saved -> Res.drawable.ic_bookmark
            AppDestination.Profile -> Res.drawable.ic_person
        }

internal fun AppDestination.palette(isDarkTheme: Boolean): FluidPalette =
    if (isDarkTheme) darkPalette else lightPalette

private val AppDestination.darkPalette: FluidPalette
    get() =
        when (this) {
            AppDestination.Home -> FluidPalettes.Aurora
            AppDestination.Search -> FluidPalettes.Orchid
            AppDestination.Saved -> FluidPalettes.Sunset
            AppDestination.Profile -> FluidPalettes.Ember
        }

private val AppDestination.lightPalette: FluidPalette
    get() =
        when (this) {
            AppDestination.Home -> LightBarPalettes.Aurora
            AppDestination.Search -> LightBarPalettes.Orchid
            AppDestination.Saved -> LightBarPalettes.Sunset
            AppDestination.Profile -> LightBarPalettes.Ember
        }
