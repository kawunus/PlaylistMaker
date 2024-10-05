package com.example.playlistmaker.presentation.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.sharing.SharingInteractorImpl
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.utils.creator.Creator

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractorImpl
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharingInteractor: SharingInteractorImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        settingsInteractor =
            Creator.provideSettingsInteractor(getSharedPreferences(PrefKeys.PREFS, MODE_PRIVATE))
        sharingInteractor = Creator.provideSharingInteractor(this)
        binding.themeSwitcher.isChecked = settingsInteractor.getTheme().isNight

        setContentView(binding.root)
        binding.themeSwitcher.isChecked
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareView.setOnClickListener {
            sharingInteractor.shareApp()
        }

        binding.supportView.setOnClickListener {
            sharingInteractor.shareApp()
        }


        binding.agreementView.setOnClickListener {
            sharingInteractor.openTerms()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }


}