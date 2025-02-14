package com.kawunus.playlistmaker.domain.api.search

import com.kawunus.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int>>
}