package com.example.playlistmaker.utils.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromList(trackIds: List<Int>?): String? {
        return gson.toJson(trackIds)
    }

    @TypeConverter
    fun toList(data: String?): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(data, listType)
    }
}