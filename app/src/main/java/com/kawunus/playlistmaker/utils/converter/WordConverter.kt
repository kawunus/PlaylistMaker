package com.kawunus.playlistmaker.utils.converter

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

    fun getMinuteWordFromMillis(timeInMillis: Int): String {
        val timeInMinutes = timeInMillis / 60000
        return when {
            timeInMinutes % 100 in 11..19 -> "$timeInMinutes минут"
            timeInMinutes % 10 == 1 -> "$timeInMinutes минута"
            timeInMinutes % 10 in 2..4 -> "$timeInMinutes минуты"
            else -> "$timeInMinutes минут"
        }
    }
}