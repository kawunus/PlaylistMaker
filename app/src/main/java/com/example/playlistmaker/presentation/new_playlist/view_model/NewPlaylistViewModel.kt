package com.example.playlistmaker.presentation.new_playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.presentation.new_playlist.ui.model.NewPlaylistState
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<NewPlaylistState>(NewPlaylistState.NotCreated)

    fun observeState(): LiveData<NewPlaylistState> = stateLiveData

    fun createNewPlaylist(playlist: PlaylistEntity) {
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(playlist)
            stateLiveData.value = NewPlaylistState.Created
        }
    }
}