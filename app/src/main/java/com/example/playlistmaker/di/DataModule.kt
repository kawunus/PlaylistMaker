package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.ITunesApiService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApiService> {
        val iTunesBaseUrl = "https://itunes.apple.com"

        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single {
        androidContext().getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory {
        MediaPlayer()
    }
}