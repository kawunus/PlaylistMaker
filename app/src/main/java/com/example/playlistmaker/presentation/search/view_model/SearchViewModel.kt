package com.example.playlistmaker.presentation.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.api.history.HistoryInteractor
import com.example.playlistmaker.domain.model.history.History
import com.example.playlistmaker.domain.model.search.SearchState
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.creator.Creator

class SearchViewModel(private val historyInteractor: HistoryInteractor) : ViewModel() {

    private val trackInteractor = Creator.provideTrackInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val latestRequest: String? = null
    private var isClickAllowed = true

    private val stateLiveData = MutableLiveData<SearchState>()

    init {
        renderState(SearchState.History(historyInteractor.getHistory().trackList))
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()

        private const val CLICK_DEBOUNCE_DELAY = 1000L

        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(historyInteractor: HistoryInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(historyInteractor)
                }
            }
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun onTextChanged(text: String, isFocus: Boolean) {
        if (latestRequest == text || !isFocus || text.isEmpty()) {
            return
        }

        // render

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable {
            search(text)
        }

        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun clickDebounce(): Boolean {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun search(request: String) {
        if (request.isNotEmpty()) {
            renderState(SearchState.Loading)
            trackInteractor.searchTracks(request) { foundTracks, resultCode ->
                run {
                    when (resultCode) {
                        200 -> {
                            if (foundTracks.isNotEmpty()) {
                                renderState(SearchState.Content(foundTracks))

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
        if (clickDebounce()) {
            historyInteractor.addToHistory(track)
        }
    }
}