package com.example.playlistmaker.presentation.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.presentation.playlists.ui.model.PlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun getData() {
        renderState(PlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlistList ->
                processResult(playlistList)
            }
        }
    }

    private fun processResult(playlistList: List<Playlist>) {
        if (playlistList.isEmpty()) {
            renderState(PlaylistState.Empty)
        } else renderState(PlaylistState.Content(playlistList))
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.value = state
    }
}