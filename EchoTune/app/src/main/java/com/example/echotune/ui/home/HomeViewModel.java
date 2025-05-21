package com.example.echotune.ui.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.echotune.Song;
import com.example.echotune.backend.SongDbHelper;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final SongDbHelper songDbHelper;
    private MutableLiveData<List<Song>> songsLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        songDbHelper = new SongDbHelper(application.getApplicationContext());
        songsLiveData = new MutableLiveData<>();
        loadSongs();
    }

    // Method to load songs from the database
    public void loadSongs() {
        // Create a new list to store the songs
        List<Song> songs = new ArrayList<>();
        // Add recently played songs from the database
        songs.addAll(songDbHelper.getRecentlyPlayed());
        // Add top songs from the database
        songs.addAll(songDbHelper.getTop());
        // Update LiveData
        songsLiveData.setValue(songs);
    }

    public MutableLiveData<List<Song>> getSongs() {
        return songsLiveData;
    }
}
