package com.example.playlistmaker.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.library.LibraryActivity
import com.example.playlistmaker.presentation.search.SearchActivity
import com.example.playlistmaker.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonFind.setOnClickListener {
            val findIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(findIntent)
        }

        binding.buttonLibrary.setOnClickListener {
            val libraryIntent = Intent(this@MainActivity, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        binding.buttonSettings.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}