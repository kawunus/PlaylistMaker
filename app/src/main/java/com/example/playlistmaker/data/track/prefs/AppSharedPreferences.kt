package com.example.playlistmaker.data.track.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.data.track.Track

class AppSharedPreferences(context: Context) {
    object PrefKeys {
        const val DARK_THEME = "darkTheme"
        const val PREFS = "prefs"
        const val HISTORY_LIST = "historyList"
    }

    private val converter = JsonConverter()
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PrefKeys.PREFS, MODE_PRIVATE)

    fun getTheme(): Boolean = prefs.getBoolean(PrefKeys.DARK_THEME, false)

    fun setTheme(darkTheme: Boolean) {
        prefs.edit().putBoolean(PrefKeys.DARK_THEME, darkTheme).apply()
    }

    fun getHistoryList(): List<Track> {

        val json = prefs.getString(PrefKeys.HISTORY_LIST, null)
        return converter.jsonToTrackList(json)
    }

    fun setHistoryList(list: List<Track>) {
        prefs.edit().putString(PrefKeys.HISTORY_LIST, converter.trackListToJson(list)).apply()
    }

}