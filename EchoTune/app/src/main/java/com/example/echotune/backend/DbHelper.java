package com.example.echotune.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "echotune.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            // Create tables
            db.execSQL(createSongTable());
            db.execSQL(createRecentlyPlayedTable());
            db.execSQL(createPlaylistTable());
            db.execSQL(createPlaylistSongsTable());

            // Pre-populate the song and recently played tables with sample data
            db.execSQL(initialiseSong());
            db.execSQL(initialiseRecentlyPlayed());
        } catch (Exception e) {
            System.out.println("Error creating tables " + e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // Creates the 'songs' table with various metadata fields
    private String createSongTable() {
        return "CREATE TABLE " + SongContract.SongEntry.TABLE_NAME + " (" +
                SongContract.SongEntry._ID + " INTEGER PRIMARY KEY, " +
                SongContract.SongEntry.COLUMN_NAME_TITLE + " varchar, " +
                SongContract.SongEntry.COLUMN_NAME_AUTHOR + " varchar, " +
                SongContract.SongEntry.COLUMN_NAME_PATH + " varchar, " +
                SongContract.SongEntry.COLUMN_NAME_GENRE + " varchar, "+
                SongContract.SongEntry.COLUMN_NAME_REPLAYS + " int);";
    }

    // Creates the 'recently_played' table, linked to songs via foreign key
    private String createRecentlyPlayedTable() {
        return "CREATE TABLE recently_played ( song_id INTEGER PRIMARY KEY, position INTEGER, " +
                "FOREIGN KEY(song_id) REFERENCES songs(_id));";
    }

    // Creates the 'playlists' table with playlist title as the primary key
    private String createPlaylistTable(){
        return "CREATE TABLE " + PlaylistContract.PlaylistEntry.TABLE_NAME + " (" +
                PlaylistContract.PlaylistEntry.COLUMN_NAME_TITLE + " varchar PRIMARY KEY);";
    }

    // Creates a relation table linking playlists and songs (many-to-many relationship)
    private String createPlaylistSongsTable(){
        return "CREATE TABLE " + PlaylistSongsContract.PlaylistSongsEntry.TABLE_NAME + " (" +
                PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_PLAYLIST + " varchar, " +
                PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_SONGID + " INTEGER, " +
                "FOREIGN KEY (" + PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_PLAYLIST + ") REFERENCES " +
                PlaylistContract.PlaylistEntry.TABLE_NAME + "(" + PlaylistContract.PlaylistEntry.COLUMN_NAME_TITLE + "), " +
                "FOREIGN KEY (" + PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_SONGID + ") REFERENCES " +
                SongContract.SongEntry.TABLE_NAME + "(" + SongContract.SongEntry._ID + "), " +
                "PRIMARY KEY (" + PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_PLAYLIST + ", " +
                PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_SONGID + "));";
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.SongEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS recently_played");
        db.execSQL("DROP TABLE IF EXISTS " + PlaylistContract.PlaylistEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlaylistSongsContract.PlaylistSongsEntry.TABLE_NAME);
    }

    // Populates the 'songs' table to simulate a database of songs
    private String initialiseSong() {
        return "INSERT INTO " + SongContract.SongEntry.TABLE_NAME + " (" +
                SongContract.SongEntry._ID + ", " +
                SongContract.SongEntry.COLUMN_NAME_TITLE + ", " +
                SongContract.SongEntry.COLUMN_NAME_AUTHOR + ", " +
                SongContract.SongEntry.COLUMN_NAME_PATH + ", " +
                SongContract.SongEntry.COLUMN_NAME_REPLAYS + ", " +
                SongContract.SongEntry.COLUMN_NAME_GENRE + ") VALUES " +
                "(1, 'Jazz Addicts', 'Cosimo Fogg', 'app/res/raw/song1.mp3', 0, 'Jazz'), " +
                "(2, 'Nighttime Stroll', 'Artificial.Music', 'app/res/raw/song2.mp3', 0, 'Jazz'), " +
                "(3, 'Night Out', 'LiQWYD', 'app/res/raw/song3.mp3', 0, 'Jazz'), " +
                "(4, 'Gotta Go', 'Tokyo Music', 'app/res/raw/song4.mp3', 0, 'Jazz'), " +
                "(5, 'Fortuna', 'TENETRUNNER', 'app/res/raw/song5.mp3', 0, 'Jazz'), " +
                "(6, 'Trust in Me', 'DIZARO', 'app/res/raw/song6.mp3', 0, 'Pop'), " +
                "(7, 'You Still Want Me', 'Markvard', 'app/res/raw/song6.mp3', 0, 'Pop'), " +
                "(8, 'If Only', 'Luke Bergs', 'app/res/raw/song8.mp3', 0, 'Pop'), " +
                "(9, 'White Down', 'XIBE', 'app/res/raw/song9.mp3', 0, 'Pop'), " +
                "(10, 'Everybody Knows', 'Niwel & Michel Young', 'app/res/raw/song10.mp3', 0, 'Pop'), " +
                "(11, 'Lonesome Journey', 'KeyOfMoonMusic', 'app/res/raw/song11.mp3', 0, 'Classical'), " +
                "(12, 'Guardians', 'ContextSensitive', 'app/res/raw/song12.mp3', 0, 'Classical'), " +
                "(13, 'Growing Up', 'Scott Buckley', 'app/res/raw/song13.mp3', 0, 'Classical'), " +
                "(14, 'The Medieval Banquet', 'Silvermansound', 'app/res/raw/song14.mp3', 0, 'Classical'), " +
                "(15, 'Love Waltz', 'Nikos Spiliotis', 'app/res/raw/song15.mp3', 0, 'Classical'), " +
                "(16, 'MAXIMALISM', 'PunchDeck', 'app/res/raw/song16.mp3', 0, 'Rock'), " +
                "(17, 'Thors Hammer', 'Ethan Meixsell', 'app/res/raw/song17.mp3', 0, 'Rock'), " +
                "(18, 'Oceans', 'Bobby Renz', 'app/res/raw/song18.mp3',  0, 'Rock'), " +
                "(19, 'In the Shadows', 'Ethan Meixsell', 'app/res/raw/song19.mp3', 0, 'Rock'), " +
                "(20, 'Grunge', 'Wayne John Bradley', 'app/res/raw/song20.mp3', 0, 'Rock'), " +
                "(21, 'Intro', 'Red Roses Realm', 'app/res/raw/song21.mp3', 0, 'Indie'), " +
                "(22, 'We Wont Tell Nobody', 'Josh Lumsden', 'app/res/raw/song22.mp3', 0, 'Indie'), " +
                "(23, 'With You In The Morning', 'Carl Storm', 'app/res/raw/song23.mp3', 0, 'Indie'), " +
                "(24, 'Lying Were Fine', 'Leonell Cassio', 'app/res/raw/song24.mp3', 0, 'Indie'), " +
                "(25, 'Fine Seeds', 'Thomas Gresen', 'app/res/raw/song25.mp3', 0, 'Indie'), " +
                "(26, 'Hustle', 'Peyruis', 'app/res/raw/song26.mp3', 0, 'Dance'), " +
                "(27, 'Dolce Vita', 'Peyruis', 'app/res/raw/song27.mp3', 0, 'Dance'), " +
                "(28, 'We are', 'Kisma', 'app/res/raw/song28.mp3', 0, 'Dance'), " +
                "(29, 'Summer Night', 'Erlandsson', 'app/res/raw/song29.mp3', 0, 'Dance'), " +
                "(30, 'Deflector', 'Ghostrifter', 'app/res/raw/song30.mp3', 0, 'Dance')";
    }

    // Populates the 'recently_played' table
    private String initialiseRecentlyPlayed(){
        return "INSERT INTO recently_played (song_id, position) VALUES (1, 0), (2, 1), (3, 2)";
    }
}
