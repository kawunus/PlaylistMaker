package com.kawunus.playlistmaker.data.search

import com.kawunus.playlistmaker.data.dto.Resource
import com.kawunus.playlistmaker.data.dto.TrackDto
import com.kawunus.playlistmaker.data.dto.TrackSearchRequest
import com.kawunus.playlistmaker.data.dto.TrackSearchResponse
import com.kawunus.playlistmaker.data.network.NetworkClient
import com.kawunus.playlistmaker.domain.api.search.SearchRepository
import com.kawunus.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val responseData =
                    TrackSearchResponse(results = (response as TrackSearchResponse).results.map {
                        TrackDto(
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.trackId,
                            it.country,
                            it.primaryGenreName,
                            it.releaseDate,
                            it.collectionName,
                            it.previewUrl
                        )
                    }).apply {
                        this.resultCode = 200
                    }
                val data = dtoToModel(responseData.results)
                emit(Resource.Success(data = data, resultCode = 200))
            }

            else -> {
                emit(
                    Resource.Error(
                        message = "Произошла ошибка сервера",
                        data = emptyList(),
                        resultCode = response.resultCode
                    )
                )
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