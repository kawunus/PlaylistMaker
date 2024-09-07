package com.example.playlistmaker.utils

import android.view.View
import com.example.playlistmaker.adapters.track.TrackAdapter
import com.example.playlistmaker.data.track.prefs.historyprefs.HistoryPrefs
import com.example.playlistmaker.databinding.ActivityFindBinding

class SearchHistory(
    private val binding: ActivityFindBinding,
    private val historyPrefs: HistoryPrefs
) {

    fun showList() = with(binding) {
        if (checkHistory()) {
            hideHistoryViews()
        } else {
            (recyclerView.adapter as TrackAdapter).saveData(historyPrefs.getHistoryList())
            historyButton.visibility = View.VISIBLE
            historyTextView.visibility = View.VISIBLE
            errorLinear.visibility = View.GONE
        }
    }

    fun hideHistoryViews() = with(binding) {
        historyButton.visibility = View.GONE
        historyTextView.visibility = View.GONE
    }

    fun clearHistory() = with(binding) {
        historyPrefs.setHistoryList(emptyList())
        (recyclerView.adapter as TrackAdapter).saveData(historyPrefs.getHistoryList())
    }

    private fun checkHistory(): Boolean {
        return historyPrefs.getHistoryList().isEmpty()
    }
}