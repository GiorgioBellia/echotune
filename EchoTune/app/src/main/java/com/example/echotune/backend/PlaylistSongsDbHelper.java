package com.example.echotune.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.example.echotune.Song;
import java.util.ArrayList;
import java.util.List;

// Helper class to manage the many-to-many relationship between playlists and songs
public class PlaylistSongsDbHelper {
    private final DbHelper dbHelper;
    private final String _playlist = PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_PLAYLIST;
    private final String _songId = PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_SONGID;

    public PlaylistSongsDbHelper(Context context){
        this.dbHelper = new DbHelper(context);
    }

    // Adds a song to a specific playlist by inserting a row into the playlist_songs table
    // Returns a boolean whether or not the operation succeeded
    public boolean addSongPlaylist(String playlist, int song){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(_playlist, playlist);
        values.put(_songId, song);

        if(db.insert(PlaylistSongsContract.PlaylistSongsEntry.TABLE_NAME, null, values) == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

    // Removes a song from a playlist using a combination of playlist name and song ID
    // Returns a boolean whether or not the operation succeeded
    public boolean removeSongPlaylist(String playlist, int songId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_PLAYLIST + " = ? AND " +
                PlaylistSongsContract.PlaylistSongsEntry.COLUMN_NAME_SONGID + " = ?";
        String[] whereArgs = { playlist, String.valueOf(songId) };

        if(db.delete(PlaylistSongsContract.PlaylistSongsEntry.TABLE_NAME, whereClause, whereArgs) < 1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

    // Retrieves a list of Song objects associated with a given playlist
    public List<Song> getSongsForPlaylist(String playlist){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT s.* FROM songs s INNER JOIN playlist_songs ps ON s._id = ps.song_id " +
                "WHERE ps.playlist = '" + playlist+"'", null);

        List<Song> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_TITLE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_GENRE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_PATH));
            int replays = cursor.getInt(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_REPLAYS));

            songs.add(new Song(id, title, author, genre, path, replays));
        }
        cursor.close();
        db.close();
        return songs;
    }

    // Retrieves a list of Song objects that are not part of a given playlist
    public List<Song> getSongsOutPlaylist(String playlist){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM songs s WHERE s._id NOT IN (" +
                        "SELECT ps.song_id FROM playlist_songs ps WHERE ps.playlist = ?" +
                        ")",
                new String[]{playlist}
        );

        List<Song> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_TITLE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_GENRE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_PATH));
            int replays = cursor.getInt(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_REPLAYS));

            songs.add(new Song(id, title, author, genre, path, replays));
        }
        cursor.close();
        db.close();
        return songs;
    }
}
