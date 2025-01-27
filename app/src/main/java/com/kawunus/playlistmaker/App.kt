package com.kawunus.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kawunus.playlistmaker.di.dataModule
import com.kawunus.playlistmaker.di.interactorModule
import com.kawunus.playlistmaker.di.repositoryModule
import com.kawunus.playlistmaker.di.utilModule
import com.kawunus.playlistmaker.di.viewModelModule
import com.kawunus.playlistmaker.domain.api.settings.SettingsInteractor
import com.kawunus.playlistmaker.domain.model.settings.Theme
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private var darkTheme = false
    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(repositoryModule, interactorModule, utilModule, viewModelModule, dataModule)
        }
        if (settingsInteractor.isFirstLaunch()) {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    darkTheme = true
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    darkTheme = false
                }

                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    darkTheme = false
                }

            }
            settingsInteractor.setFirstLaunchCompleted()
            settingsInteractor.setTheme(Theme(isNight = darkTheme))
        } else {
            darkTheme = settingsInteractor.getTheme().isNight
        }

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchTheme(isNight: Boolean) {
        darkTheme = isNight
        settingsInteractor.setTheme(theme = Theme(isNight))
        AppCompatDelegate.setDefaultNightMode(
            if (isNight) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}