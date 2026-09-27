package dev.hyperiontech.fluid_bottom_bar.feature.home.domain

import kotlinx.coroutines.flow.Flow

interface HomeFeedRepository {
    fun observeHomeFeed(): Flow<HomeFeed>
}
