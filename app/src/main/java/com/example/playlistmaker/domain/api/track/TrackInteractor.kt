package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int>>
}