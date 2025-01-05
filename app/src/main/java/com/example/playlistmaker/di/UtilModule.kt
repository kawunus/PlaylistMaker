package com.example.playlistmaker.di

import com.example.playlistmaker.utils.converter.JsonConverter
import com.example.playlistmaker.utils.converter.TrackConverter
import org.koin.dsl.module

val utilModule = module {
    single {
        JsonConverter()
    }

    single {
        TrackConverter()
    }
}