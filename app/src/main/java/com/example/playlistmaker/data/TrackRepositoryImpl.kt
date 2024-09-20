package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): TrackSearchResponse {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return if (response.resultCode == 200) {
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
        } else {
            TrackSearchResponse(results = emptyList()).apply {
                this.resultCode = response.resultCode
            }
        }
    }
}