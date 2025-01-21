package com.example.playlistmaker.presentation.playlist_info.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistInfoViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist
) : ViewModel() {
}