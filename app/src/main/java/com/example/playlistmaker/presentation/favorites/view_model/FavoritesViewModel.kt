package com.example.playlistmaker.presentation.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.favorites.ui.model.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val trackInteractor: TrackInteractor) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()

    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun getData() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            trackInteractor.getFavoritesTracks().collect { trackList ->
                processResult(trackList)
            }
        }
    }

    private fun processResult(trackList: List<Track>) {
        if (trackList.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else renderState(FavoritesState.Content(trackList))
    }

    private fun renderState(state: FavoritesState) {
        stateLiveData.value = state
    }
}