package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.data.dto.TrackSearchResponse

interface TrackRepository {
    fun searchTracks(expression: String): TrackSearchResponse
}