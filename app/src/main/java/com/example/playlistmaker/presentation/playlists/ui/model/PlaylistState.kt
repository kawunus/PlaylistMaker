package com.example.playlistmaker.presentation.playlists.ui.model

import com.example.playlistmaker.domain.model.playlist.Playlist

sealed interface PlaylistState {

    data object Loading : PlaylistState

    data class Content(val playlistList: List<Playlist>) : PlaylistState

    data object Empty : PlaylistState

}