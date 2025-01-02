package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.data.dto.TrackSearchResponse
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<TrackSearchResponse>>
}