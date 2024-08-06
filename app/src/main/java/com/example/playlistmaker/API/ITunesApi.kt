package com.example.playlistmaker.API

import com.example.playlistmaker.API.responces.TrackResponce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponce>
}