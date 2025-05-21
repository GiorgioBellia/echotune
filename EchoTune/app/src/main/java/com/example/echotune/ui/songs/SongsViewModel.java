package com.example.echotune.ui.discover;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.echotune.Song;
import com.example.echotune.backend.PlaylistSongsDbHelper;
import java.util.List;

public class SongsViewModel extends AndroidViewModel {
    private final PlaylistSongsDbHelper playlistSongsDbHelper;
    private final MutableLiveData<List<Song>> nonPlaylistSongsLiveData;
    private final MutableLiveData<List<Song>> playlistSongsLiveData;

    public SongsViewModel(@NonNull Application application) {
        super(application);
        playlistSongsDbHelper = new PlaylistSongsDbHelper(application.getApplicationContext());
        // LiveData holding the list of songs NOT in the selected playlist
        nonPlaylistSongsLiveData = new MutableLiveData<>();
        // LiveData holding the list of songs in the selected playlist
        playlistSongsLiveData = new MutableLiveData<>();
    }

    /**
     * Loads songs that are NOT part of the given playlist from the database.
     * Updates nonPlaylistSongsLiveData with the results.
     */
    public void loadNonPlaylistSongs(String playlist) {
        List<Song> songs = playlistSongsDbHelper.getSongsOutPlaylist(playlist);
        nonPlaylistSongsLiveData.setValue(songs);
    }

    public LiveData<List<Song>> getNonPlaylistSongs() {
        return nonPlaylistSongsLiveData;
    }

    /**
     * Loads songs that ARE part of the given playlist from the database.
     * Updates playlistSongsLiveData with the results.
     */
    public void loadPlaylistSongs(String playlist){
        List<Song> songs = playlistSongsDbHelper.getSongsForPlaylist(playlist);
        playlistSongsLiveData.setValue(songs);
    }

    public LiveData<List<Song>> getPlaylistSongs() {
        return playlistSongsLiveData;
    }
}
