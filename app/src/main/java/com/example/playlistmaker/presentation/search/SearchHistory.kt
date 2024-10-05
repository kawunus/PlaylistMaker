package com.example.playlistmaker.presentation.search

import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.history.HistoryInteractor
import com.example.playlistmaker.domain.model.history.History
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.search.activity.TrackAdapter

class SearchHistory(
    private val binding: ActivitySearchBinding,
    private val historyInteractor: HistoryInteractor,
) {

    fun showList() = with(binding) {
        (historyRecyclerView.adapter as TrackAdapter).saveData(historyInteractor.getHistory().trackList)
        if (checkHistory()) {
            hideHistoryViews()
        } else {
            historyButton.isVisible = true
            historyTextView.isVisible = true
            errorLinear.isVisible = false
            historyRecyclerView.isVisible = true
            recyclerView.isVisible = false
        }
    }

    fun hideHistoryViews() = with(binding) {
        historyRecyclerView.isVisible = false
        historyButton.isVisible = false
        historyTextView.isVisible = false
        recyclerView.isVisible = true
    }

    fun clearHistory() = with(binding) {
        setHistory(emptyList())
        (recyclerView.adapter as TrackAdapter).saveData(historyInteractor.getHistory().trackList)
    }

    private fun checkHistory(): Boolean {
        return historyInteractor.getHistory().trackList.isEmpty()
    }

    private fun setHistory(trackList: List<Track>) {
        historyInteractor.setHistory(History(trackList))
    }

    fun getHistory(): List<Track> {
        return historyInteractor.getHistory().trackList
    }
}