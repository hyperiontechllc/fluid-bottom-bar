package dev.hyperiontech.fluid_bottom_bar.feature.home.domain

enum class ArticleCategory { Tech, Architecture, DesignSystems, Engineering, Trends }

data class Article(
    val id: String,
    val title: String,
    val summary: String,
    val category: ArticleCategory,
    val readTimeMinutes: Int,
)

data class ReadingProgress(
    val article: Article,
    val part: Int,
    val totalParts: Int,
    val completedFraction: Float,
    val minutesLeft: Int,
)

data class HomeFeed(
    val readerName: String,
    val featured: Article,
    val inProgress: List<ReadingProgress>,
    val curated: List<Article>,
)
