package com.example.echotune;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.echotune.backend.PlaylistSongsDbHelper;
import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {
    private final PlaylistSongsDbHelper playlistSongsDbHelper;
    private final MutableLiveData<List<Song>> songsLiveData = new MutableLiveData<>();

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        playlistSongsDbHelper = new PlaylistSongsDbHelper(application);
    }

    public LiveData<List<Song>> getSongsLiveData() {
        return songsLiveData;
    }

    public void setPlaylist(String playlist){
        loadSongs(playlist);
    }

    public void loadSongs(String playlist) {
        List<Song> songs = playlistSongsDbHelper.getSongsForPlaylist(playlist);
        songsLiveData.setValue(songs);
    }

    public boolean addSong(String playlist, int songId) {
        boolean added = playlistSongsDbHelper.addSongPlaylist(playlist, songId);
        if (added) loadSongs(playlist);
        return added;
    }

    public boolean removeSong(String playlist, int songId) {
        boolean removed = playlistSongsDbHelper.removeSongPlaylist(playlist, songId);
        if (removed) loadSongs(playlist);
        return removed;
    }
}
