package com.kawunus.playlistmaker.presentation.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kawunus.playlistmaker.core.ui.BaseViewModel
import com.kawunus.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.presentation.playlists.ui.model.PlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : BaseViewModel() {

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