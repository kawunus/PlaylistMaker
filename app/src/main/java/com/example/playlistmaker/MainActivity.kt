package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonFind = findViewById<Button>(R.id.buttonFind)
        val buttonLibrary = findViewById<Button>(R.id.buttonLibrary)
        val buttonSettings = findViewById<Button>(R.id.buttonSettings)

        buttonFind.setOnClickListener {
            val findIntent = Intent(this, FindActivity::class.java)
            startActivity(findIntent)
        }
        buttonLibrary.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        val cardSettingsListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }

        buttonSettings.setOnClickListener(cardSettingsListener)
    }
}