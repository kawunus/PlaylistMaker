package com.example.playlistmaker.presentation.new_playlist.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.presentation.new_playlist.ui.model.NewPlaylistState
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<NewPlaylistState>(NewPlaylistState.NotCreated)

    fun observeState(): LiveData<NewPlaylistState> = stateLiveData

    fun createNewPlaylist(name: String, description: String?, imageUrl: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(
                PlaylistDto(
                    name = name,
                    description = description,
                    imageUrl = imageUrl
                )
            )
            stateLiveData.value = NewPlaylistState.Created
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
            stateLiveData.value = NewPlaylistState.Created
        }
    }
}