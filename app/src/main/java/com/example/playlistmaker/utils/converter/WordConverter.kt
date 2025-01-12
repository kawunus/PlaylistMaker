package com.example.playlistmaker.utils.converter

object WordConverter {
    fun getTrackWordForm(count: Int): String {
        val absCount = count % 100
        val lastDigit = count % 10

        return when {
            absCount in 11..19 -> "треков"
            lastDigit == 1 -> "трек"
            lastDigit in 2..4 -> "трека"
            else -> "треков"
        }
    }
}