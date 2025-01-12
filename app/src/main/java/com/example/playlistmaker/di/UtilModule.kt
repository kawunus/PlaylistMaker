package com.example.playlistmaker.di

import com.example.playlistmaker.utils.converter.JsonConverter
import com.example.playlistmaker.utils.converter.PlaylistConverter
import com.example.playlistmaker.utils.converter.TrackConverter
import org.koin.dsl.module

val utilModule = module {
    factory {
        JsonConverter()
    }

    factory {
        TrackConverter()
    }

    factory {
        PlaylistConverter()
    }
}