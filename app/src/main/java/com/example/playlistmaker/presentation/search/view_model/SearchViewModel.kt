package com.example.playlistmaker.presentation.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.history.HistoryInteractor
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.model.history.History
import com.example.playlistmaker.domain.model.search.SearchState
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyInteractor: HistoryInteractor, private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var latestRequest: String? = null
    private var isClickAllowed = true

    private val stateLiveData = MutableLiveData<SearchState>()

    init {
        renderState(SearchState.History(historyInteractor.getHistory().trackList))
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()

        private const val CLICK_DEBOUNCE_DELAY = 1000L

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun observeState(): LiveData<SearchState> = stateLiveData

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun onTextChanged(text: String) {
        if (latestRequest == text || text.isEmpty()) {
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
            return
        }

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        latestRequest = text

        val searchRunnable = Runnable {
            search(text)
        }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        renderState(SearchState.PreLoading)
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    fun clickDebounce(): Boolean {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime({ isClickAllowed = true }, SEARCH_REQUEST_TOKEN, postTime)
        }
        return current
    }

    fun search(request: String) {
        if (request.isNotBlank() && request.isNotEmpty() && stateLiveData.value == SearchState.PreLoading) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                trackInteractor.searchTracks(request).collect { pair ->
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
        } else {
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        }
    }

    private fun getHistory(): List<Track> {
        return historyInteractor.getHistory().trackList
    }

    fun showHistory() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        renderState(SearchState.History(getHistory()))
    }

    fun clearHistory() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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