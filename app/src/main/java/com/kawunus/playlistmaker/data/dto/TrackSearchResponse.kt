package com.kawunus.playlistmaker.data.dto

data class TrackSearchResponse(
    val results: List<TrackDto>
) : Response()