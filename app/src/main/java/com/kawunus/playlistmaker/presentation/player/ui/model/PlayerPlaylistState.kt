package com.kawunus.playlistmaker.presentation.player.ui.model

import com.kawunus.playlistmaker.domain.model.playlist.Playlist

sealed interface PlayerPlaylistState {

    data class Content(val playlistList: List<Playlist>) : PlayerPlaylistState

    data object Empty : PlayerPlaylistState
}