package com.example.playlistmaker.presentation.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.favorites.FavoriteTrackInteractor
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.favorites.ui.model.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()

    fun observeState(): LiveData<FavoritesState> = stateLiveData

    init {
        renderState(FavoritesState.Loading)
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            favoriteTrackInteractor.getFavoritesTracks().collect { trackList ->
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