package com.example.echotune.backend;

// Contract class that defines the schema for the 'playlist_songs' relation table
public final class PlaylistSongsContract {
    private PlaylistSongsContract() {}

    // Inner class defining table contents for mapping songs to playlists
    public static class PlaylistSongsEntry {
        // Name of the table
        public static final String TABLE_NAME = "playlist_songs";

        // Column name representing the playlist title (foreign key to playlists table)
        public static final String COLUMN_NAME_PLAYLIST = "playlist";

        // Column name representing the song ID (foreign key to songs table)
        public static final String COLUMN_NAME_SONGID = "song_id";
    }
}
