package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val shareView =
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.shareView)
        val supportView =
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.supportView)
        val agreementView =
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.agreementView)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        shareView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val message = "https://practicum.yandex.ru/android-developer/"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            val chooser = Intent.createChooser(intent, getString(R.string.share_title))
            startActivity(chooser)
        }

        supportView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val message = getString(R.string.support_message)
            val title = getString(R.string.support_title)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_SUBJECT, title)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("markovtsev.stas@yandex.by"))

            startActivity(intent)
        }


        agreementView.setOnClickListener {
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val intent = Intent(Intent.ACTION_VIEW, url)

            startActivity(intent)
        }
    }


}