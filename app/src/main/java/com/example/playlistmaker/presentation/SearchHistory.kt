package com.example.playlistmaker.presentation

import android.view.View
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.domain.models.track.prefs.historyprefs.HistoryPrefs
import com.example.playlistmaker.presentation.find.TrackAdapter

class SearchHistory(
    private val binding: ActivityFindBinding,
    private val historyPrefs: HistoryPrefs
) {

    fun showList() = with(binding) {
        (historyRecyclerView.adapter as TrackAdapter).saveData(historyPrefs.getHistoryList())
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
        historyPrefs.setHistoryList(emptyList())
        (recyclerView.adapter as TrackAdapter).saveData(historyPrefs.getHistoryList())
    }

    private fun checkHistory(): Boolean {
        return historyPrefs.getHistoryList().isEmpty()
    }
}