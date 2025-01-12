package com.example.playlistmaker.utils.converter

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistConverter {

    fun map(playlist: PlaylistEntity): Playlist = Playlist(
        name = playlist.name,
        description = playlist.description,
        id = playlist.id ?: 0,
        countOfTracks = playlist.countOfTracks,
        imageUrl = playlist.imageUrl,
        imageName = playlist.imageName
    )
}