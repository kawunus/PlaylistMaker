package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.models.track.prefs.PrefKeys
import com.example.playlistmaker.domain.models.track.prefs.ThemePrefs

class App : Application() {
    private var darkTheme = false
    private lateinit var themePrefs: ThemePrefs
    override fun onCreate() {
        super.onCreate()
        themePrefs = ThemePrefs(
            getSharedPreferences(
                PrefKeys.PREFS, MODE_PRIVATE
            )
        )
        darkTheme = themePrefs.getTheme()
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        themePrefs.setTheme(darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}