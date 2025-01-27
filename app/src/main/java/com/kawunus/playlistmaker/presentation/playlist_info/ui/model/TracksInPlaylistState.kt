package com.kawunus.playlistmaker.presentation.playlist_info.ui.model

import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.domain.model.track.Track

sealed interface TracksInPlaylistState {

    data class Empty(val playlist: Playlist) : TracksInPlaylistState

    data object Loading : TracksInPlaylistState

    data class Content(val trackList: List<Track>, val playlist: Playlist) : TracksInPlaylistState
}