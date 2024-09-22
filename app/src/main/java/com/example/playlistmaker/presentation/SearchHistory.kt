package com.example.playlistmaker.presentation

import android.view.View
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.history.HistoryInteractor
import com.example.playlistmaker.domain.model.history.History
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.search.TrackAdapter

class SearchHistory(
    private val binding: ActivitySearchBinding,
    private val historyInteractor: HistoryInteractor,
) {

    fun showList() = with(binding) {
        (historyRecyclerView.adapter as TrackAdapter).saveData(historyInteractor.getHistory().trackList)
        if (checkHistory()) {
            hideHistoryViews()
        } else {
            historyButton.visibility = View.VISIBLE
            historyTextView.visibility = View.VISIBLE
            errorLinear.visibility = View.GONE
            historyRecyclerView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    fun hideHistoryViews() = with(binding) {
        historyRecyclerView.visibility = View.GONE
        historyButton.visibility = View.GONE
        historyTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun clearHistory() = with(binding) {
        setHistory(emptyList())
        (recyclerView.adapter as TrackAdapter).saveData(historyInteractor.getHistory().trackList)
    }

    private fun checkHistory(): Boolean {
        return historyInteractor.getHistory().trackList.isEmpty()
    }

    fun setHistory(trackList: List<Track>) {
        historyInteractor.setHistory(History(trackList))
    }

    fun getHistory(): List<Track> {
        return historyInteractor.getHistory().trackList
    }
}