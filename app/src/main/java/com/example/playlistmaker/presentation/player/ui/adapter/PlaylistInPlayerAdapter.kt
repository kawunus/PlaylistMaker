package com.example.playlistmaker.presentation.player.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistInPlayerAdapter(
    private val onItemClick: ((playlist: Playlist) -> Unit)?
) : RecyclerView.Adapter<PlaylistInPlayerViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInPlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_in_player, parent, false)
        return PlaylistInPlayerViewHolder(view)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: PlaylistInPlayerViewHolder, position: Int) {
        val playlist = asyncListDiffer.currentList[position]
        holder.bind(asyncListDiffer.currentList[position])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlist)
        }
    }
}