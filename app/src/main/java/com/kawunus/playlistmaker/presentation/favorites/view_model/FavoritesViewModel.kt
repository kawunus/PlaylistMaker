package com.kawunus.playlistmaker.presentation.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kawunus.playlistmaker.core.ui.BaseViewModel
import com.kawunus.playlistmaker.domain.api.favorite.FavoriteTrackInteractor
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.favorites.ui.model.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()

    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun getData() {
        renderState(FavoritesState.Loading)
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