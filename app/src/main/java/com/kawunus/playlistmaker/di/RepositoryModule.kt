package com.kawunus.playlistmaker.di

import com.kawunus.playlistmaker.data.favorites.FavoriteTrackRepositoryImpl
import com.kawunus.playlistmaker.data.file_manager.FileManager
import com.kawunus.playlistmaker.data.history.HistoryRepositoryImpl
import com.kawunus.playlistmaker.data.network.NetworkClient
import com.kawunus.playlistmaker.data.network.RetrofitNetworkClient
import com.kawunus.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.kawunus.playlistmaker.data.playlist.PlaylistRepositoryImpl
import com.kawunus.playlistmaker.data.search.SearchRepositoryImpl
import com.kawunus.playlistmaker.data.settings.SettingsRepositoryImpl
import com.kawunus.playlistmaker.data.sharing.ExternalNavigatorIml
import com.kawunus.playlistmaker.domain.api.favorite.FavoriteTrackRepository
import com.kawunus.playlistmaker.domain.api.history.HistoryRepository
import com.kawunus.playlistmaker.domain.api.player.MediaPlayerRepository
import com.kawunus.playlistmaker.domain.api.playlist.PlaylistRepository
import com.kawunus.playlistmaker.domain.api.search.SearchRepository
import com.kawunus.playlistmaker.domain.api.settings.SettingsRepository
import com.kawunus.playlistmaker.domain.api.sharing.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(
            mediaPlayer = get()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorIml(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get(), get(), get())
    }

    single {
        FileManager(androidContext())
    }
}