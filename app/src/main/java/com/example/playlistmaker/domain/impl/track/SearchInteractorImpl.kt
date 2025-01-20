package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

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