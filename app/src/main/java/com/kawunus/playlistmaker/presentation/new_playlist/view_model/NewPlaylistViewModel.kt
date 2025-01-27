package com.kawunus.playlistmaker.presentation.new_playlist.view_model

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.kawunus.playlistmaker.data.dto.PlaylistDto
import com.kawunus.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.presentation.new_playlist.ui.model.NewPlaylistState
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor, private val analytics: FirebaseAnalytics
) : ViewModel() {

    private val stateLiveData = MutableLiveData<NewPlaylistState>(NewPlaylistState.NotCreated)

    fun observeState(): LiveData<NewPlaylistState> = stateLiveData

    fun createNewPlaylist(name: String, description: String?, imageUrl: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(
                PlaylistDto(
                    name = name.trim(), description = description?.trim(), imageUrl = imageUrl
                )
            )
            stateLiveData.value = NewPlaylistState.Created
            val analyticsBundle = Bundle().apply {
                putString("Playlist name", name.trim())
                putString(
                    "Playlist description", description?.trim()
                )
            }
            analytics.logEvent("Create_Playlist", analyticsBundle)
        }
    }

    fun updatePlaylist(name: String, description: String?, imageUrl: Uri?, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(
                PlaylistDto(
                    name = name.trim(), description = description?.trim(), imageUrl = imageUrl
                ), playlist
            )
            stateLiveData.value = NewPlaylistState.Created
        }
    }
}