package com.example.playlistmaker.utils.snackbar

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.playlistmaker.R
import com.google.android.material.snackbar.Snackbar

object Snackbar {

    fun showSnackbar(
        view: View,
        message: String,
        context: Context
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.textSize = 14f
        textView.setTextColor(ContextCompat.getColor(context, R.color.snackbarTextColor))
        snackbar.view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.snackbarBackgroundColor
            )
        )
        val typeface = ResourcesCompat.getFont(context, R.font.yandex_sans_display_regular)
        textView.typeface = typeface
        snackbar.show()
    }
}