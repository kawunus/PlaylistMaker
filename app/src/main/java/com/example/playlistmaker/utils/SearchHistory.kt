package com.example.playlistmaker.utils

import android.view.View
import com.example.playlistmaker.adapters.track.TrackAdapter
import com.example.playlistmaker.data.track.prefs.AppSharedPreferences
import com.example.playlistmaker.ui.activities.FindActivity

class SearchHistory(private val context: FindActivity) {

    val appSharedPreferences: AppSharedPreferences = AppSharedPreferences(context)

    fun showList() = with(context.binding) {
        (recyclerView.adapter as TrackAdapter).saveData(appSharedPreferences.getHistoryList())
        historyButton.visibility = View.VISIBLE
        historyTextView.visibility = View.VISIBLE
        errorLinear.visibility = View.GONE
    }

    fun hideHistory() = with(context.binding) {
        historyButton.visibility = View.GONE
        historyTextView.visibility = View.GONE

    }

    fun clearHistory() = with(context.binding) {
        appSharedPreferences.setHistoryList(emptyList())
        (recyclerView.adapter as TrackAdapter).saveData(appSharedPreferences.getHistoryList())
    }
}