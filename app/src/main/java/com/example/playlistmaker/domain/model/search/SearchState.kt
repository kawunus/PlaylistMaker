package com.example.playlistmaker.domain.model.search

import com.example.playlistmaker.domain.model.track.Track

sealed interface SearchState {

    data object Loading : SearchState

    data class Content(val trackList: List<Track>) : SearchState

    data class Error(val errorMessage: String) : SearchState

    data class Empty(val message: String) : SearchState

    data object History : SearchState

}