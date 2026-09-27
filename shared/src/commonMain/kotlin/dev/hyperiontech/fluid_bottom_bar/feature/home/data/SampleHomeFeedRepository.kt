package dev.hyperiontech.fluid_bottom_bar.feature.home.data

import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.Article
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.ArticleCategory
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.HomeFeed
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.HomeFeedRepository
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.ReadingProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SampleHomeFeedRepository : HomeFeedRepository {
    override fun observeHomeFeed(): Flow<HomeFeed> = flowOf(value = SampleHomeFeed)
}

private val SampleHomeFeed =
    HomeFeed(
        readerName = "Avatar",
        featured =
            Article(
                id = "designing-better-ux",
                title = "Designing Better User Experiences",
                summary = "Principles for crafting interfaces people love to use.",
                category = ArticleCategory.Tech,
                readTimeMinutes = 5,
            ),
        inProgress =
            listOf(
                ReadingProgress(
                    article =
                        Article(
                            id = "spatial-ui-fluid-canvas",
                            title = "Modern Spatial UI & Fluid Canvas",
                            summary = "Composing depth-aware layouts on a fluid canvas.",
                            category = ArticleCategory.Architecture,
                            readTimeMinutes = 12,
                        ),
                    part = 2,
                    totalParts = 4,
                    completedFraction = 0.65f,
                    minutesLeft = 4,
                ),
                ReadingProgress(
                    article =
                        Article(
                            id = "design-tokens-at-scale",
                            title = "Designing Design Tokens at Scale",
                            summary = "Structuring tokens that survive growing product surfaces.",
                            category = ArticleCategory.DesignSystems,
                            readTimeMinutes = 11,
                        ),
                    part = 1,
                    totalParts = 3,
                    completedFraction = 0.30f,
                    minutesLeft = 8,
                ),
            ),
        curated =
            listOf(
                Article(
                    id = "micro-frontends-2025",
                    title = "The Evolution of Micro-Frontends in 2025",
                    summary = "Architecting distributed frontend systems with zero build overhead.",
                    category = ArticleCategory.Engineering,
                    readTimeMinutes = 6,
                ),
                Article(
                    id = "spatial-interfaces",
                    title = "Spatial Interfaces: Designing Beyond Flat Screens",
                    summary = "Deep dive into depth, gestures, and tactile audio-visual feedback loops.",
                    category = ArticleCategory.Trends,
                    readTimeMinutes = 4,
                ),
            ),
    )
