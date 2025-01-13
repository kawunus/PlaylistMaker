package com.example.playlistmaker.di

import com.example.playlistmaker.domain.api.favorite.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.history.HistoryInteractor
import com.example.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.sharing.SharingInteractor
import com.example.playlistmaker.domain.impl.favorites.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.history.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.player.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.sharing.SharingInteractorImpl
import com.example.playlistmaker.domain.impl.track.SearchInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}