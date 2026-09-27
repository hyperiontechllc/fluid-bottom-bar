package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.ArticleCategory
import dev.hyperiontech.fluid_bottom_bar.ui.theme.FluidColors
import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.category_architecture
import fluidbottombar.shared.generated.resources.category_design_systems
import fluidbottombar.shared.generated.resources.category_engineering
import fluidbottombar.shared.generated.resources.category_tech
import fluidbottombar.shared.generated.resources.category_trends
import fluidbottombar.shared.generated.resources.ic_aperture
import fluidbottombar.shared.generated.resources.ic_layers
import fluidbottombar.shared.generated.resources.ic_sparkle
import fluidbottombar.shared.generated.resources.ic_star
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Immutable
internal data class ArticleCategoryStyle(
    val label: StringResource,
    val accent: Color,
    val artwork: List<Color>,
    val icon: DrawableResource,
)

internal val ArticleCategory.style: ArticleCategoryStyle
    get() =
        when (this) {
            ArticleCategory.Tech -> TechStyle
            ArticleCategory.Architecture -> ArchitectureStyle
            ArticleCategory.DesignSystems -> DesignSystemsStyle
            ArticleCategory.Engineering -> EngineeringStyle
            ArticleCategory.Trends -> TrendsStyle
        }

private val TechStyle by lazy(mode = LazyThreadSafetyMode.NONE) {
    ArticleCategoryStyle(
        label = Res.string.category_tech,
        accent = FluidColors.Fuchsia300,
        artwork = listOf(FluidColors.Violet600, FluidColors.Pink600, FluidColors.Orange500),
        icon = Res.drawable.ic_sparkle,
    )
}

private val ArchitectureStyle by lazy(mode = LazyThreadSafetyMode.NONE) {
    ArticleCategoryStyle(
        label = Res.string.category_architecture,
        accent = FluidColors.Cyan400,
        artwork = listOf(FluidColors.Violet600, FluidColors.Indigo600, FluidColors.Cyan400),
        icon = Res.drawable.ic_sparkle,
    )
}

private val DesignSystemsStyle by lazy(mode = LazyThreadSafetyMode.NONE) {
    ArticleCategoryStyle(
        label = Res.string.category_design_systems,
        accent = FluidColors.Rose400,
        artwork = listOf(FluidColors.Rose500, FluidColors.Fuchsia600, FluidColors.Amber400),
        icon = Res.drawable.ic_layers,
    )
}

private val EngineeringStyle by lazy(mode = LazyThreadSafetyMode.NONE) {
    ArticleCategoryStyle(
        label = Res.string.category_engineering,
        accent = FluidColors.Cyan400,
        artwork = listOf(FluidColors.Indigo500, FluidColors.Purple500, FluidColors.Pink500),
        icon = Res.drawable.ic_aperture,
    )
}

private val TrendsStyle by lazy(mode = LazyThreadSafetyMode.NONE) {
    ArticleCategoryStyle(
        label = Res.string.category_trends,
        accent = FluidColors.Amber400,
        artwork = listOf(FluidColors.Amber500, FluidColors.Orange600, FluidColors.Rose600),
        icon = Res.drawable.ic_star,
    )
}
