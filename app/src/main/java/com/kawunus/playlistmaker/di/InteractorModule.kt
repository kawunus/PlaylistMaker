package com.kawunus.playlistmaker.di

import com.kawunus.playlistmaker.domain.api.favorite.FavoriteTrackInteractor
import com.kawunus.playlistmaker.domain.api.history.HistoryInteractor
import com.kawunus.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.kawunus.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.kawunus.playlistmaker.domain.api.search.SearchInteractor
import com.kawunus.playlistmaker.domain.api.settings.SettingsInteractor
import com.kawunus.playlistmaker.domain.api.sharing.SharingInteractor
import com.kawunus.playlistmaker.domain.impl.favorites.FavoriteTrackInteractorImpl
import com.kawunus.playlistmaker.domain.impl.history.HistoryInteractorImpl
import com.kawunus.playlistmaker.domain.impl.player.MediaPlayerInteractorImpl
import com.kawunus.playlistmaker.domain.impl.playlist.PlaylistInteractorImpl
import com.kawunus.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.kawunus.playlistmaker.domain.impl.sharing.SharingInteractorImpl
import com.kawunus.playlistmaker.domain.impl.track.SearchInteractorImpl
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