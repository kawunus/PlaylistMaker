package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}