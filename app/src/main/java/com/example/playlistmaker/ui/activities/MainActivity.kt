package com.example.playlistmaker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonFind.setOnClickListener {
            val findIntent = Intent(this@MainActivity, FindActivity::class.java)
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