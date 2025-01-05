package com.example.playlistmaker.presentation.search.ui.model

import com.example.playlistmaker.domain.model.track.Track

sealed interface SearchState {

    data object Loading : SearchState

    data class Content(val trackList: List<Track>) : SearchState

    data object Error : SearchState

    data object Empty : SearchState

    data class History(val historyList: List<Track>) : SearchState

    data object PreLoading : SearchState

}