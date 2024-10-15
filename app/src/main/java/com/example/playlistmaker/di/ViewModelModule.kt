package com.example.playlistmaker.di

import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import com.example.playlistmaker.presentation.track.view_model.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (track: Track) ->
        TrackViewModel(track, get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get(), get())
    }

}