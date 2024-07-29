package com.example.playlistmaker.data.track.prefs.historyprefs

import android.content.SharedPreferences
import com.example.playlistmaker.data.track.Track
import com.example.playlistmaker.data.track.prefs.PrefKeys

class HistoryPrefs(private val prefs:SharedPreferences) {


    private val converter = JsonConverter()

    fun getHistoryList(): List<Track> {

        val json = prefs.getString(PrefKeys.HISTORY_LIST, null)
        return converter.jsonToTrackList(json)
    }

    fun setHistoryList(list: List<Track>) {
        prefs.edit().putString(PrefKeys.HISTORY_LIST, converter.trackListToJson(list)).apply()
    }

    fun addToHistoryList(track: Track) {
        val oldTrackList = getHistoryList().toMutableList()
        if (oldTrackList.none { it.trackId == track.trackId }) {
            if (oldTrackList.size >= 10) {
                oldTrackList.removeLast()
            }
            oldTrackList.add(0, track)
            setHistoryList(oldTrackList)
        }
    }
}