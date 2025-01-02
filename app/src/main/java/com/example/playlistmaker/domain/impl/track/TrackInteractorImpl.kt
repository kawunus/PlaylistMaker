package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.data.dto.TrackDto
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
                    Pair(dtoToModel(result.data.results), result.data.resultCode)
                }

                is Resource.Error -> {
                    Pair(null, result.data.resultCode)
                }
            }
        }

    }


    private fun dtoToModel(resultList: List<TrackDto>): List<Track> {
        val trackList = mutableListOf<Track>()

        for (trackDto in resultList) {
            val track = Track(
                trackName = trackDto.trackName,
                artistName = trackDto.artistName,
                trackTimeMillis = trackDto.trackTimeMillis,
                artworkUrl100 = trackDto.artworkUrl100,
                trackId = trackDto.trackId,
                country = trackDto.country,
                primaryGenreName = trackDto.primaryGenreName,
                releaseDate = trackDto.releaseDate,
                collectionName = trackDto.collectionName,
                previewUrl = trackDto.previewUrl
            )
            trackList.add(track)
        }

        return trackList.toList()
    }
}