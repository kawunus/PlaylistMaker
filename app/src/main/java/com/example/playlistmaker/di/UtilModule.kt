package com.example.playlistmaker.di

import com.example.playlistmaker.utils.converter.FavoriteTrackConverter
import com.example.playlistmaker.utils.converter.JsonConverter
import com.example.playlistmaker.utils.converter.PlaylistConverter
import com.example.playlistmaker.utils.converter.TrackConverter
import org.koin.dsl.module

val utilModule = module {
    factory {
        JsonConverter()
    }

    factory {
        FavoriteTrackConverter()
    }

    factory {
        PlaylistConverter()
    }

    factory {
        TrackConverter()
    }
}