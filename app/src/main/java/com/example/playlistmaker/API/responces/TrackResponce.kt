package com.example.playlistmaker.API.responces

import com.example.playlistmaker.data.track.Track

class TrackResponce(
    val resultCount: Int,
    val results: List<Track>
)