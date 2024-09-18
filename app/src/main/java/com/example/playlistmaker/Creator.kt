package com.example.playlistmaker

import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

class Creator {
    private fun getTrackRepository() = TrackRepositoryImpl(RetrofitNetworkClient())

    fun provideTrackInteractor() = TrackInteractorImpl(getTrackRepository())
}