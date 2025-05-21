package com.example.echotune.backend;

import android.provider.BaseColumns;

// Contract class that defines the schema for the 'playlists' table
public final class PlaylistContract {
    private PlaylistContract() {}

    // Inner class that defines the table contents
    public static class PlaylistEntry implements BaseColumns {
        // Inherits BaseColumns, which provides the "_ID" column automatically

        // Name of the table
        public static final String TABLE_NAME = "playlists";

        // Column for the playlist title (used as the primary key)
        public static final String COLUMN_NAME_TITLE = "_title";
    }
}
