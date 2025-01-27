package com.kawunus.playlistmaker.domain.api.history

import com.kawunus.playlistmaker.domain.model.history.History
import com.kawunus.playlistmaker.domain.model.track.Track

interface HistoryInteractor {
    fun getHistory(): History

    fun setHistory(history: History)

    fun addToHistory(track: Track)
}