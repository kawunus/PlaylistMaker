package com.example.playlistmaker.data.track.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AppSharedPreferences(context: Context) {
    object PrefKeys {
        const val DARK_THEME = "dark_theme"
        const val PREFS = "prefs"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PrefKeys.PREFS, MODE_PRIVATE)

    fun getTheme(): Boolean = prefs.getBoolean(PrefKeys.DARK_THEME, false)

    fun setTheme(darkTheme: Boolean) {
        prefs.edit().putBoolean(PrefKeys.DARK_THEME, darkTheme).apply()
    }
}