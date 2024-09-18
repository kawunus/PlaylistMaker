package com.example.playlistmaker.domain.models.track.prefs.historyprefs

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.track.Track
import com.example.playlistmaker.domain.models.track.prefs.PrefKeys

class HistoryPrefs(private val prefs: SharedPreferences) {


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
        val existingTrackIndex = oldTrackList.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            oldTrackList.removeAt(existingTrackIndex)
        }
        oldTrackList.add(0, track)
        if (oldTrackList.size > 10) {
            oldTrackList.removeLast()
        }
        setHistoryList(oldTrackList)
    }

}