package com.kawunus.playlistmaker.utils.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kawunus.playlistmaker.domain.model.track.Track

class JsonConverter {

    fun trackListToJson(trackList: List<Track>): String {

        return Gson().toJson(trackList)
    }

    fun jsonToTrackList(json: String?): List<Track> {
        if (json == null) {
            return emptyList()
        }
        val trackListType = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, trackListType)
    }
}