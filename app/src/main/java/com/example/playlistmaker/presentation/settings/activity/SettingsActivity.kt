package com.example.playlistmaker.presentation.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory(context = this)
        )[SettingsViewModel::class]

        viewModel.observeThemeLiveData().observe(this) { isNight ->
            binding.themeSwitcher.isChecked = isNight
            (applicationContext as App).switchTheme(isNight)
        }

        setContentView(binding.root)
        binding.themeSwitcher.isChecked
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareView.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportView.setOnClickListener {
            viewModel.openSupport()
        }


        binding.agreementView.setOnClickListener {
            viewModel.openTerms()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }


}