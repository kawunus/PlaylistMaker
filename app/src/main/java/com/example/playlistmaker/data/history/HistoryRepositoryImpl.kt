package com.example.playlistmaker.data.history

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.history.HistoryRepository
import com.example.playlistmaker.domain.model.history.History
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.HistoryPrefs
import com.example.playlistmaker.utils.converter.JsonConverter

class HistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : HistoryRepository {

    private val converter = JsonConverter()

    override fun getHistory(): History {
        val json = sharedPreferences.getString(HistoryPrefs.HISTORY.name, null)
        return History(converter.jsonToTrackList(json))
    }

    override fun setHistory(history: History) {
        sharedPreferences.edit()
            .putString(HistoryPrefs.HISTORY.name, converter.trackListToJson(history.trackList))
            .apply()
    }

    override fun addToHistory(track: Track) {
        val oldTrackList = getHistory().trackList.toMutableList()
        val existingTrackIndex = oldTrackList.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            oldTrackList.removeAt(existingTrackIndex)
        }
        oldTrackList.add(0, track)
        if (oldTrackList.size > 10) {
            oldTrackList.removeLast()
        }
        setHistory(History(oldTrackList))
    }
}