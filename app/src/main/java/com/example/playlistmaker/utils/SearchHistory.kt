package com.example.playlistmaker.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.playlistmaker.adapters.track.TrackAdapter
import com.example.playlistmaker.data.track.prefs.PrefKeys
import com.example.playlistmaker.data.track.prefs.historyprefs.HistoryPrefs
import com.example.playlistmaker.ui.activities.FindActivity

class SearchHistory(private val context: FindActivity) {

    private val historyPrefs = HistoryPrefs(
        context.getSharedPreferences(
            PrefKeys.PREFS, MODE_PRIVATE
        )
    )

    fun showList() = with(context.binding) {
        (recyclerView.adapter as TrackAdapter).saveData(historyPrefs.getHistoryList())
        if (checkHistory()) {
            hideHistoryViews()
        } else {
            historyButton.visibility = View.VISIBLE
            historyTextView.visibility = View.VISIBLE
            errorLinear.visibility = View.GONE
        }
    }

    fun hideHistoryViews() = with(context.binding) {
        historyButton.visibility = View.GONE
        historyTextView.visibility = View.GONE

    }

    fun clearHistory() = with(context.binding) {
        historyPrefs.setHistoryList(emptyList())
        (recyclerView.adapter as TrackAdapter).saveData(historyPrefs.getHistoryList())
    }

    private fun checkHistory(): Boolean {
        return historyPrefs.getHistoryList().isEmpty()
    }
}