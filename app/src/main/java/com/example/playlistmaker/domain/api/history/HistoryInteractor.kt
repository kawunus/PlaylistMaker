package com.example.playlistmaker.domain.api.history

import com.example.playlistmaker.domain.model.history.History
import com.example.playlistmaker.domain.model.track.Track

interface HistoryInteractor {
    fun getHistory(): History

    fun setHistory(history: History)

    fun addToHistory(track: Track)
}