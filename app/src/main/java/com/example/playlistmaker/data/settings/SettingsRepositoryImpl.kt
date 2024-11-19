package com.example.playlistmaker.data.settings

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.settings.SettingsRepository
import com.example.playlistmaker.domain.model.settings.Theme
import com.example.playlistmaker.utils.consts.SettingsPrefs

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,

    ) : SettingsRepository {
    override fun getTheme(): Theme {
        return Theme(sharedPreferences.getBoolean(SettingsPrefs.THEME.name, false))
    }

    override fun setTheme(theme: Theme) {
        sharedPreferences.edit().putBoolean(SettingsPrefs.THEME.name, theme.isNight).apply()
    }

    override fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(SettingsPrefs.FIRST_LAUNCH.name, true)
    }

    override fun setFirstLaunchCompleted() {
        sharedPreferences.edit().putBoolean(SettingsPrefs.FIRST_LAUNCH.name, false).apply()
    }
}