package com.kawunus.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.kawunus.playlistmaker.data.db.AppDatabase
import com.kawunus.playlistmaker.data.network.ITunesApiService
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

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    single { get<AppDatabase>().favoriteTrackDao() }

    single { get<AppDatabase>().playlistDao() }

    single { get<AppDatabase>().trackDao() }

    single { get<AppDatabase>().playlistTrackDao() }
}