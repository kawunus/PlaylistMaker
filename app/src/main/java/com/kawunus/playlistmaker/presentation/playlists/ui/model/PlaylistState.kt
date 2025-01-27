package com.kawunus.playlistmaker.presentation.playlists.ui.model

import com.kawunus.playlistmaker.domain.model.playlist.Playlist

sealed interface PlaylistState {

    data object Loading : PlaylistState

    data class Content(val playlistList: List<Playlist>) : PlaylistState

    data object Empty : PlaylistState

}