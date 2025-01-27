package com.kawunus.playlistmaker.data.network

import com.kawunus.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackSearchResponse

}