package com.kawunus.playlistmaker.presentation.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.domain.model.track.Track

class TrackAdapter(
    private val onItemClick: ((track: Track) -> Unit)?,
    private val onLongItemClick: ((track: Track) -> Unit)?
) :
    RecyclerView.Adapter<TrackViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(trackList: List<Track>) {
        asyncListDiffer.submitList(trackList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = asyncListDiffer.currentList[position]
        holder.bind(asyncListDiffer.currentList[position])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(track)
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(track)
            true
        }
    }

    override fun getItemCount() = asyncListDiffer.currentList.size
}