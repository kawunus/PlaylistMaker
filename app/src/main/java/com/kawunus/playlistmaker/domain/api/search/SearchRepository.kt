package com.kawunus.playlistmaker.domain.api.search

import com.kawunus.playlistmaker.data.dto.Resource
import com.kawunus.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}