package com.example.playlistmaker.utils.creator

import android.content.SharedPreferences
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.track.TrackInteractorImpl

class Creator {
    private fun getTrackRepository() = TrackRepositoryImpl(RetrofitNetworkClient())

    fun provideTrackInteractor() = TrackInteractorImpl(getTrackRepository())

    private fun getSettingsRepository(sharedPreferences: SharedPreferences) =
        SettingsRepositoryImpl(sharedPreferences)

    fun provideSettingsInteractor(sharedPreferences: SharedPreferences) =
        SettingsInteractorImpl(getSettingsRepository(sharedPreferences))
}