package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.track.prefs.AppSharedPreferences

class App : Application() {
    private var darkTheme = false
    private lateinit var appPreferences: AppSharedPreferences
    override fun onCreate() {
        super.onCreate()
        appPreferences = AppSharedPreferences(this@App)
        darkTheme = appPreferences.getTheme()
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        appPreferences.setTheme(darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}