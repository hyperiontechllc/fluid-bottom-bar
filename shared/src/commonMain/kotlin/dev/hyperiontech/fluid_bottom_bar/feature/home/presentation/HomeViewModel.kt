package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.HomeFeed
import dev.hyperiontech.fluid_bottom_bar.feature.home.domain.HomeFeedRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val feed: HomeFeed,
    ) : HomeUiState
}

class HomeViewModel(
    homeFeedRepository: HomeFeedRepository,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> =
        homeFeedRepository
            .observeHomeFeed()
            .map(transform = HomeUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = STOP_TIMEOUT_MILLIS),
                initialValue = HomeUiState.Loading,
            )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
