package com.kawunus.playlistmaker.di

import com.kawunus.playlistmaker.utils.converter.FavoriteTrackConverter
import com.kawunus.playlistmaker.utils.converter.JsonConverter
import com.kawunus.playlistmaker.utils.converter.PlaylistConverter
import com.kawunus.playlistmaker.utils.converter.TrackConverter
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