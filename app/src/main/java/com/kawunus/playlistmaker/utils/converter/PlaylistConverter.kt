package com.kawunus.playlistmaker.utils.converter

import com.kawunus.playlistmaker.data.db.entity.PlaylistEntity
import com.kawunus.playlistmaker.domain.model.playlist.Playlist

class PlaylistConverter {

    fun map(playlist: PlaylistEntity): Playlist = Playlist(
        name = playlist.name,
        description = playlist.description,
        id = playlist.id ?: 0,
        countOfTracks = playlist.countOfTracks,
        imageUrl = playlist.imageUrl,
        imageName = playlist.imageName
    )

    fun map(playlist: Playlist): PlaylistEntity = PlaylistEntity(
        name = playlist.name,
        description = playlist.description,
        id = playlist.id ?: 0,
        countOfTracks = playlist.countOfTracks,
        imageUrl = playlist.imageUrl,
        imageName = playlist.imageName
    )
}