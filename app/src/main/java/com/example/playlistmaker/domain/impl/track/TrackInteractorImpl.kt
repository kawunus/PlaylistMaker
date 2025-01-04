package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int>> {

        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, result.resultCode)
                }

                is Resource.Error -> {
                    Pair(null, result.resultCode)
                }
            }
        }

    }
}