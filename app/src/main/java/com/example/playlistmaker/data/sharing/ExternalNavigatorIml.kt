package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.EmailData
import com.example.playlistmaker.domain.api.sharing.ExternalNavigator

class ExternalNavigatorIml(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.type = "text/plain"
        val chooser = Intent.createChooser(intent, context.getString(R.string.share_title))
        context.startActivity(chooser)
    }

    override fun openLink(link: String) {
        val url = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, url)

        context.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_SUBJECT, emailData.title)
        intent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))

        context.startActivity(intent)
    }

    override fun getShareAppLink(): String = context.getString(R.string.link_on_course)

    override fun getSupportEmailData(): EmailData = EmailData(
        message = context.getString(R.string.support_message),
        title = context.getString(R.string.support_title),
        email = context.getString(R.string.dev_email)
    )

    override fun getTermsLink(): String = context.getString(R.string.link_to_agreement)
}