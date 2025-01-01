package com.example.playlistmaker.data.track

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.api.track.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): Flow<Resource<TrackSearchResponse>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {

                val data =
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

                emit(Resource.Success(data))
            }

            else -> {
                val data = TrackSearchResponse(
                    results = emptyList(),
                ).apply { this.resultCode = response.resultCode }
                emit(
                    Resource.Error(
                        message = "Произошла ошибка сервера", data = data
                    )
                )
            }
        }
    }
}