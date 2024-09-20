package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.model.track.Track
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val response = repository.searchTracks(expression)
            consumer.consume(dtoToModel(response.results), response.resultCode)
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