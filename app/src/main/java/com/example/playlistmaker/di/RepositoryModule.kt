package com.example.playlistmaker.di

import com.example.playlistmaker.data.history.HistoryRepositoryImpl
import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorIml
import com.example.playlistmaker.data.track.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.history.HistoryRepository
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.api.settings.SettingsRepository
import com.example.playlistmaker.domain.api.sharing.ExternalNavigator
import com.example.playlistmaker.domain.api.track.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    factory<MediaPlayerRepository> { (onPrepared: () -> Unit, onCompletion: () -> Unit, onSetTimer: (time: String) -> Unit) ->
        MediaPlayerRepositoryImpl(
            mediaPlayer = get(),
            onPrepared = onPrepared,
            onCompletion = onCompletion,
            onSetTimer = onSetTimer
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorIml(get())
    }
}