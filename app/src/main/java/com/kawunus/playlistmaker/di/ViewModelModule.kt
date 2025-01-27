package com.kawunus.playlistmaker.di

import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.kawunus.playlistmaker.presentation.new_playlist.view_model.NewPlaylistViewModel
import com.kawunus.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.kawunus.playlistmaker.presentation.playlist_info.view_model.PlaylistInfoViewModel
import com.kawunus.playlistmaker.presentation.playlists.view_model.PlaylistViewModel
import com.kawunus.playlistmaker.presentation.search.view_model.SearchViewModel
import com.kawunus.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
    viewModel {
        PlaylistViewModel(get())
    }
    viewModel {
        NewPlaylistViewModel(get())
    }
    viewModel { (playlist: Playlist) ->
        PlaylistInfoViewModel(get(), playlist, get())
    }
}