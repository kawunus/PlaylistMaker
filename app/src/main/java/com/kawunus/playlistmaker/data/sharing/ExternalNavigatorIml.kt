package com.kawunus.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.data.dto.EmailData
import com.kawunus.playlistmaker.domain.api.sharing.ExternalNavigator
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.utils.converter.WordConverter

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

    override fun sharePlaylist(
        trackList: List<Track>,
        playlistName: String,
        playlistDescription: String?
    ) {
        val message = getPlaylistMessage(trackList, playlistName, playlistDescription)

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        val chooser = Intent.createChooser(intent, context.getString(R.string.share_playlist_title))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun getPlaylistMessage(
        trackList: List<Track>,
        playlistName: String,
        playlistDescription: String?
    ): String {
        val tracksCount = "${trackList.size} ${WordConverter.getTrackWordForm(trackList.size)}\n"
        val trackMessage = trackList.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${
                WordConverter.getMinuteWordFromMillis(
                    track.trackTimeMillis
                )
            })"
        }.joinToString("\n")
        val title = "Плейлист $playlistName \n"
        val description =
            if (playlistDescription.isNullOrEmpty()) {
                ""
            } else {
                "Описание: $playlistDescription \n"
            }
        val message = title + description + tracksCount + trackMessage

        return message
    }
}