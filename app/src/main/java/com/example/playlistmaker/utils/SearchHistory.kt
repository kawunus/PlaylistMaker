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