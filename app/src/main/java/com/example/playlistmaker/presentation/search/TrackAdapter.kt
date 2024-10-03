package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.track.Track

class TrackAdapter(
    private val onItemClick: ((track: Track) -> Unit)?
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = asyncListDiffer.currentList[position]
        holder.bind(asyncListDiffer.currentList[position])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(track)
        }
    }

    override fun getItemCount() = asyncListDiffer.currentList.size
}