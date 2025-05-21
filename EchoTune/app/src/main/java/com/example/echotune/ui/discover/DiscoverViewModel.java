package com.example.echotune.ui.discover;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.echotune.Song;
import com.example.echotune.backend.SongDbHelper;
import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {
    private final SongDbHelper songDbHelper;
    private final MutableLiveData<List<Song>> allSongsLiveData;
    private final MutableLiveData<List<String>> allGenresLiveData;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        songDbHelper = new SongDbHelper(application.getApplicationContext());
        allGenresLiveData = new MutableLiveData<>();
        allSongsLiveData = new MutableLiveData<>();
        loadAllSongs();
        loadAllGenres();
    }

    /**
     * Loads all songs from the database and updates the LiveData
     */
    public void loadAllSongs() {
        List<Song> songs = songDbHelper.getSongs();
        allSongsLiveData.setValue(songs);
    }

    /**
     * Loads songs filtered by genre from the database and updates the LiveData
     */
    public void loadSongsByGenre(String genre) {
        List<Song> songs = songDbHelper.getSongsByGenre(genre);
        allSongsLiveData.setValue(songs);
    }

    public LiveData<List<Song>> getAllSongs() {
        return allSongsLiveData;
    }

    /**
     * Loads all genres from the database and updates the LiveData
     */
    public void loadAllGenres(){
        List<String> genres = songDbHelper.getGenres();
        allGenresLiveData.setValue(genres);
    }

    public LiveData<List<String>> getAllGenres() {
        return allGenresLiveData;
    }
}
