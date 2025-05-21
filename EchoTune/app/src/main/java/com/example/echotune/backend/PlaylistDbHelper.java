package com.example.echotune.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

// Helper class to interact with the playlists table in the database
public class PlaylistDbHelper {
    private final DbHelper dbHelper;
    private final String _title = PlaylistContract.PlaylistEntry.COLUMN_NAME_TITLE;

    public PlaylistDbHelper(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    // Inserts a new playlist with the given name into the database
    // Returns a boolean whether or not the operation succeeded
    public boolean createPlaylist(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(_title, name);
        if (db.insert(PlaylistContract.PlaylistEntry.TABLE_NAME, null, values) == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

    // Retrieves all playlist titles from the database
    public List<String> getPlaylists(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                PlaylistContract.PlaylistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<String> playlists = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            playlists.add(title);
        }

        cursor.close();
        db.close();
        return playlists;
    }
}
