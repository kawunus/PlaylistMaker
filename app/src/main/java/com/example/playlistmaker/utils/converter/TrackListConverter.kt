package com.example.playlistmaker.utils.converter

import androidx.room.TypeConverter
import com.example.playlistmaker.domain.model.track.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackListConverter {

    @TypeConverter
    fun fromTrackList(trackList: List<Track>?): String? {
        return Gson().toJson(trackList)
    }

    @TypeConverter
    fun toTrackList(trackListString: String?): List<Track>? {
        if (trackListString.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(trackListString, type)
    }

}