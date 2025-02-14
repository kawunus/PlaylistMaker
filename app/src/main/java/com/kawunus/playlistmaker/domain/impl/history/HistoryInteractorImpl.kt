package com.kawunus.playlistmaker.domain.impl.history

import com.kawunus.playlistmaker.domain.api.history.HistoryInteractor
import com.kawunus.playlistmaker.domain.api.history.HistoryRepository
import com.kawunus.playlistmaker.domain.model.history.History
import com.kawunus.playlistmaker.domain.model.track.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun getHistory(): History {
        return historyRepository.getHistory()
    }

    override fun setHistory(history: History) {
        historyRepository.setHistory(history)
    }

    override fun addToHistory(track: Track) {
        historyRepository.addToHistory(track)
    }
}