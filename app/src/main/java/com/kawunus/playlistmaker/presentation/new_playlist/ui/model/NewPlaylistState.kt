package com.kawunus.playlistmaker.presentation.new_playlist.ui.model

sealed interface NewPlaylistState {

    data object NotCreated : NewPlaylistState

    data object Created : NewPlaylistState
}