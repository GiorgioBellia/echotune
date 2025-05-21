package com.example.echotune;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter for displaying playlists in a RecyclerView
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private List<String> playlistList;
    private OnPlaylistClickListener listener;

    public PlaylistAdapter(List<String> playlistList, OnPlaylistClickListener listener) {
        this.playlistList = playlistList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_card, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        // Get the playlist name for this position
        String playlist = playlistList.get(position);
        // Set playlist title text
        holder.playlistName.setText(playlist);

        // Set click listener to notify when a playlist is selected
        holder.itemView.setOnClickListener(v -> {
                    listener.onPlaylistClick(playlist);
                }
        );
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    // ViewHolder class to hold and recycle playlists views
    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistName;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistName = itemView.findViewById(R.id.playlist_title);
        }
    }

    // Update the adapter data and refresh the RecyclerView
    public void updatePlaylists(List<String> newPlaylists){
        this.playlistList = newPlaylists;
        notifyDataSetChanged();
    }

    // Interface to handle click events on playlists
    public interface OnPlaylistClickListener {
        void onPlaylistClick(String playlist);
    }
}
