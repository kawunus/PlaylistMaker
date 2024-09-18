package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackSearchResponse

interface TrackRepository {
    fun searchTracks(expression: String): TrackSearchResponse
}