package com.example.playlistmaker.domain.models.track.prefs

import android.content.SharedPreferences

class ThemePrefs(private val prefs: SharedPreferences) {
    fun getTheme(): Boolean = prefs.getBoolean(PrefKeys.DARK_THEME, false)

    fun setTheme(darkTheme: Boolean) {
        prefs.edit().putBoolean(PrefKeys.DARK_THEME, darkTheme).apply()
    }
}