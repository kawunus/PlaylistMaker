package com.example.playlistmaker.presentation.new_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createNewPlaylist(playlist: PlaylistEntity) {
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(playlist)
        }
    }
}