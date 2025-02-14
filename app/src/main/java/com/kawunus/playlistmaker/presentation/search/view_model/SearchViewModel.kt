package com.kawunus.playlistmaker.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kawunus.playlistmaker.core.ui.BaseViewModel
import com.kawunus.playlistmaker.domain.api.history.HistoryInteractor
import com.kawunus.playlistmaker.domain.api.search.SearchInteractor
import com.kawunus.playlistmaker.domain.model.history.History
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.search.ui.model.SearchState
import com.kawunus.playlistmaker.utils.debounce.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyInteractor: HistoryInteractor, private val searchInteractor: SearchInteractor
) : BaseViewModel() {

    private var latestRequest: String? = null

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            search(changedText)
        }

    private val stateLiveData = MutableLiveData<SearchState>()

    init {
        renderState(SearchState.History(historyInteractor.getHistory().trackList))
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun observeState(): LiveData<SearchState> = stateLiveData

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun searchDebounce(changedText: String) {
        if (latestRequest == changedText) {
            return
        }
        this.latestRequest = changedText
        renderState(SearchState.PreLoading)
        trackSearchDebounce(changedText)
    }

    fun search(request: String) {
        if (request.isNotBlank() && request.isNotEmpty() && stateLiveData.value == SearchState.PreLoading) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                searchInteractor.searchTracks(request).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        } else showHistory()
    }

    private fun processResult(foundTracks: List<Track>?, resultCode: Int) {
        if (stateLiveData.value == SearchState.Loading) {
            when (resultCode) {
                200 -> {
                    if (foundTracks != null) {
                        if (foundTracks.isNotEmpty()) {
                            renderState(SearchState.Content(foundTracks))
                        } else renderState(SearchState.Empty)
                    } else renderState(SearchState.Empty)
                }

                400 -> {
                    renderState(SearchState.Error)
                }

                else -> {
                    renderState(SearchState.Error)
                }
            }
        }
    }

    private fun getHistory(): List<Track> {
        return historyInteractor.getHistory().trackList
    }

    fun showHistory() {

        renderState(SearchState.History(getHistory()))
    }

    fun clearHistory() {

        historyInteractor.setHistory(History(emptyList()))
        showHistory()
    }

    fun clearRequestText() {
        showHistory()
    }

    fun addToHistory(track: Track) {
        historyInteractor.addToHistory(track)

        if (stateLiveData.value is SearchState.History) {
            renderState(SearchState.History(getHistory()))
        }
    }
}