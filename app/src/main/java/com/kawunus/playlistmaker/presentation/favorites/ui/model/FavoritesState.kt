package com.kawunus.playlistmaker.presentation.favorites.ui.model

import com.kawunus.playlistmaker.domain.model.track.Track

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(val trackList: List<Track>) : FavoritesState

    data object Empty : FavoritesState
}