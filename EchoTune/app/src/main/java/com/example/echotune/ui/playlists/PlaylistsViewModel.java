package com.example.echotune.ui.playlists;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.echotune.backend.PlaylistDbHelper;
import java.util.List;

public class PlaylistsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> playlists;
    private final PlaylistDbHelper playlistDbHelper;

    public PlaylistsViewModel(@NonNull Application application) {
        super(application);
        playlistDbHelper = new PlaylistDbHelper(application.getApplicationContext());
        playlists = new MutableLiveData<>();
        loadPlaylists();
    }

    public LiveData<List<String>> getPlaylists() {
        return playlists;
    }

    // Method to load playlists from the database
    public void loadPlaylists() {
        List<String> list = playlistDbHelper.getPlaylists();
        playlists.setValue(list);
    }

    // Method to create a playlist
    public boolean createPlaylist(String name){
        return playlistDbHelper.createPlaylist(name);
    }
}
