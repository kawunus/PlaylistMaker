package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val message = getString(R.string.link_on_course)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            val chooser = Intent.createChooser(intent, getString(R.string.share_title))
            startActivity(chooser)
        }

        binding.supportView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val message = getString(R.string.support_message)
            val title = getString(R.string.support_title)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_SUBJECT, title)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))

            startActivity(intent)
        }


        binding.agreementView.setOnClickListener {
            val url = Uri.parse(getString(R.string.link_to_agreement))
            val intent = Intent(Intent.ACTION_VIEW, url)

            startActivity(intent)
        }
    }


}