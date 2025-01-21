package com.example.playlistmaker.presentation.playlist_info.ui.model

import com.example.playlistmaker.domain.model.track.Track

sealed interface TracksInPlaylistState {

    data object Empty : TracksInPlaylistState

    data object Loading : TracksInPlaylistState

    data class Content(val trackList: List<Track>) : TracksInPlaylistState
}