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
public class SongDbHelper {
    private final DbHelper dbHelper;
    private final String _title = SongContract.SongEntry.COLUMN_NAME_TITLE;
    private final String _author = SongContract.SongEntry.COLUMN_NAME_AUTHOR;
    private final String _genre = SongContract.SongEntry.COLUMN_NAME_GENRE;
    private final String _path = SongContract.SongEntry.COLUMN_NAME_PATH;
    private final String _replays = SongContract.SongEntry.COLUMN_NAME_REPLAYS;

    public SongDbHelper(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    // Retrieves all songs from the database
    public List<Song> getSongs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SongContract.SongEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<Song> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(_author));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(_path));
            int replays = cursor.getInt(cursor.getColumnIndexOrThrow(_replays));

            songs.add(new Song(id, title, author, genre, path, replays));
        }
        cursor.close();
        db.close();
        return songs;
    }

    // Retrieves the top 3 most replayed songs
    public List<Song> getTop(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _title,
                _author,
                _path,
                _genre,
                _replays
        };

        // Order by descending replays and get the top 3
        Cursor cursor = db.query(
                SongContract.SongEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                _replays + " Desc",
                "3");

        List<Song> topSongs = new ArrayList<>();
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(_author));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(_path));
            int replays = cursor.getInt(cursor.getColumnIndexOrThrow(_replays));
            topSongs.add(new Song(id, title, author, genre, path, replays));
        }
        cursor.close();
        db.close();
        return topSongs;
    }

    // Increments the replay count of a song by 1
    public void incrementReplays(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = { _replays };
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(id) };

        Cursor cursor = db.query(
                SongContract.SongEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int currentReplays = cursor.getInt(cursor.getColumnIndexOrThrow(_replays));
            int newReplays = currentReplays + 1;

            ContentValues values = new ContentValues();
            values.put(_replays, newReplays);

            db.update(
                    SongContract.SongEntry.TABLE_NAME,
                    values,
                    BaseColumns._ID + " = ?",
                    new String[]{ Long.toString(id) }
            );
        }
        cursor.close();
        db.close();
    }

    // Retrieves the list of recently played songs in order
    public List<Song> getRecentlyPlayed(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT s.* FROM songs s " +
                        "JOIN recently_played r ON s._id = r.song_id ORDER BY r.position",
                null
        );

        List<Song> recentlyPlayed = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(_author));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(_path));
            int replays = cursor.getInt(cursor.getColumnIndexOrThrow(_replays));
            recentlyPlayed.add(new Song(id, title, author,  genre, path, replays));
        }
        cursor.close();
        db.close();
        return recentlyPlayed;
    }

    // Overwrites the list of recently played songs in the table
    public void setRecentlyPlayed(List<Song> songs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete("recently_played", null, null);

        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            ContentValues values = new ContentValues();
            values.put("song_id", song.getId());
            values.put("position", i);
            db.insert("recently_played", null, values);
        }
        db.close();
    }

    // Returns a list of distinct genres in the database
    public List<String> getGenres() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT "+_genre+ " FROM "+ SongContract.SongEntry.TABLE_NAME,
                null
        );

        List<String> genres = new ArrayList<>();
        while (cursor.moveToNext()) {
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            genres.add(genre);
        }
        cursor.close();
        db.close();
        return genres;
    }

    // Retrieves songs that belong to a specific genre
    public List<Song> getSongsByGenre(String genre) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM songs WHERE _genre = '"+genre+"'", null);

        List<Song> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(_author));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(_path));
            int replays = cursor.getInt(cursor.getColumnIndexOrThrow(_replays));
            songs.add(new Song(id, title, author, genre, path, replays));
        }
        cursor.close();
        db.close();
        return songs;
    }
}
