package com.example.echotune;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter for displaying Song items in different contexts (normal, add, remove)
public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_ADD = 1;
    private static final int VIEW_TYPE_REMOVE = 2;
    private List<Song> songList;
    private int adapterType;

    public SongAdapter(List<Song> songList, int adapterType) {
        this.songList = songList;
        this.adapterType = adapterType;
    }

    public int getItemViewType(int position) {
        return adapterType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL){
            // Inflate normal song card layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card, parent, false);
            return new songViewHolder(view);
        }else if (viewType == VIEW_TYPE_ADD){
            // Inflate layout for adding a song
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_song_card, parent, false);
            return new addSongViewHolder(view);
        }else {
            // Inflate layout for removing a song
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remove_song_card, parent, false);
            return new removeSongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Song song = songList.get(position);
        // Bind data based on view holder type
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL){
            ((songViewHolder) holder).bind(song);
        }else if (holder.getItemViewType() == VIEW_TYPE_ADD){
            ((addSongViewHolder) holder).bind(song);
        }else if (holder.getItemViewType() == VIEW_TYPE_REMOVE) {
            ((removeSongViewHolder) holder).bind(song);
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    // Update the adapter's song list and refresh the UI
    public void updateSongs(List<Song> newSongs) {
        this.songList = newSongs;
        notifyDataSetChanged();
    }

    // ViewHolder for normal song card
    static class songViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songAuthor;
        ImageButton playButton;
        RelativeLayout cardLayout;

        public songViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.text_title);
            songAuthor = itemView.findViewById(R.id.text_author);
            playButton = itemView.findViewById(R.id.button_play);
            cardLayout = itemView.findViewById(R.id.song_card);
        }

        public void bind(Song song){
            songTitle.setText(song.getTitle());
            songAuthor.setText(song.getAuthor());
            cardLayout.setTag(song);
        }
    }

    // ViewHolder for add-song card
    static class addSongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songAuthor;
        ImageButton addButton;
        RelativeLayout cardLayout;

        public addSongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.text_title);
            songAuthor = itemView.findViewById(R.id.text_author);
            addButton = itemView.findViewById(R.id.add_button);
            cardLayout = itemView.findViewById(R.id.song_card);
        }

        public void bind(Song song){
            songTitle.setText(song.getTitle());
            songAuthor.setText(song.getAuthor());
            cardLayout.setTag(song);
        }
    }

    // ViewHolder for remove-song card
    static class removeSongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songAuthor;
        ImageButton removeButton;
        RelativeLayout cardLayout;

        public removeSongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.text_title);
            songAuthor = itemView.findViewById(R.id.text_author);
            removeButton = itemView.findViewById(R.id.button_remove);
            cardLayout = itemView.findViewById(R.id.song_card);
        }

        public void bind(Song song){
            songTitle.setText(song.getTitle());
            songAuthor.setText(song.getAuthor());
            cardLayout.setTag(song);
        }
    }
}