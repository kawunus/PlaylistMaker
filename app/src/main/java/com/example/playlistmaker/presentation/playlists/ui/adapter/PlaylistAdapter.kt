package com.example.playlistmaker.presentation.playlists.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistAdapter(
    private val onItemClick: ((playlist: Playlist) -> Unit)?
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(playlistList: List<Playlist>) {
        asyncListDiffer.submitList(playlistList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_in_library, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = asyncListDiffer.currentList[position]
        holder.bind(asyncListDiffer.currentList[position])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlist)
        }
    }
}