package com.example.echotune.backend;

import android.provider.BaseColumns;

// Contract class that defines the schema for the "songs" table in the database
public final class SongContract {
    private SongContract() {}

    // Inner class that defines the columns of the songs table
    public static class SongEntry implements BaseColumns {
        // Inherits BaseColumns, which provides the "_ID" column automatically

        // Name of the table
        public static final String TABLE_NAME = "songs";

        // Column names for song properties
        public static final String COLUMN_NAME_TITLE = "_title";
        public static final String COLUMN_NAME_AUTHOR = "_author";
        public static final String COLUMN_NAME_GENRE = "_genre";
        public static final String COLUMN_NAME_PATH = "_path";
        public static final String COLUMN_NAME_REPLAYS = "_replays";
    }
}
