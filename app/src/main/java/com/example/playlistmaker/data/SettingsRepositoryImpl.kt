package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.settings.SettingsRepository
import com.example.playlistmaker.domain.model.settings.Theme
import com.example.playlistmaker.utils.consts.SettingsPrefs

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,

    ) : SettingsRepository {
    override fun getTheme(): Theme {
        return Theme(sharedPreferences.getBoolean(SettingsPrefs.THEME, false))
    }
}