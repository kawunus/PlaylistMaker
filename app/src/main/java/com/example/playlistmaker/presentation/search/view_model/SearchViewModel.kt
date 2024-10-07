package com.example.playlistmaker.presentation.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
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

    private val stateLiveData = MutableLiveData<SearchState>(SearchState.History)

    companion object {

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

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun addToHistory(track: Track) {
        historyInteractor.addToHistory(track)
    }

    private val historyListLiveData =
        MutableLiveData(historyInteractor.getHistory().trackList)

    private val trackLiveData = MutableLiveData<List<Track>>(emptyList())

    fun observeHistory(): LiveData<List<Track>> = historyListLiveData

    fun observeTrackList(): LiveData<List<Track>> = trackLiveData

    fun showHistory() {
        stateLiveData.postValue(SearchState.History)
    }

    fun getHistory() {
        historyListLiveData.postValue(historyInteractor.getHistory().trackList)
    }

    fun clearHistory() {
        historyInteractor.setHistory(History(emptyList()))
    }

    fun search(request: String) {
        if (request.isNotEmpty()) {
            renderState(SearchState.Loading)

            trackInteractor.searchTracks(
                request
            ) { foundTracks, resultCode ->
                run {
                    //if (editText.text.toString() == request) {
                    when (resultCode) {
                        200 -> {
                            if (foundTracks.isNotEmpty()) {
                                trackLiveData.postValue(foundTracks)
                                renderState(SearchState.Content(foundTracks))

                            } else renderState(SearchState.Empty)

                        }

                        400 -> {
                            renderState(SearchState.Error)
                        }

                        else -> {
                            renderState(SearchState.Error)
                        }
                        //  }
                    }
                }
            }
        }
    }

    fun searchDebounce(request: String) {
        if (request.isNotEmpty()) {
            val searchRunnable = Runnable {
                search(request)
            }
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }
}