package com.example.echotune;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.echotune.backend.PlaylistSongsDbHelper;
import com.example.echotune.backend.SongDbHelper;
import com.example.echotune.ui.home.HomeViewModel;
import java.util.List;

/**
 * ViewModel for the PlayingActivity, managing playback state and data operations.
 */
public class PlayingViewModel extends AndroidViewModel {
    private final SongDbHelper songDbHelper;
    private final PlaylistSongsDbHelper playlistSongsDbHelper;
    private final MutableLiveData<Song> currentSong = new MutableLiveData<>();
    private final MutableLiveData<List<Song>> playlist = new MutableLiveData<>();

    public PlayingViewModel(@NonNull Application application) {
        super(application);
        songDbHelper = new SongDbHelper(application.getApplicationContext());
        playlistSongsDbHelper = new PlaylistSongsDbHelper(application.getApplicationContext());
    }

    /**
     * Initialize playlist (either full song list or a specific playlist).
     */
    public void setPlaylist(String playlistName) {
        List<Song> songs;
        if (playlistName != null){
            songs = playlistSongsDbHelper.getSongsForPlaylist(playlistName);
        }else{
            songs = songDbHelper.getSongs();
        }

        playlist.setValue(songs);
    }

    /**
     * Select and start playing a song.
     */
    public void playSong(Song song) {
        currentSong.setValue(song);

        // Manage recently played list: remove duplicates, add current at front
        List<Song> recentPlayed = songDbHelper.getRecentlyPlayed();
        recentPlayed.removeIf(previouslyPlayed -> song.getId() == previouslyPlayed.getId());
        recentPlayed.add(0, song);
        if (recentPlayed.size() > 3) {
            recentPlayed.remove(recentPlayed.size() - 1);
        }
        songDbHelper.setRecentlyPlayed(recentPlayed);

        // Increment replay count for the song
        songDbHelper.incrementReplays(song.getId());
    }

    /**
     * Play next song in the current playlist, or wrap if at end.
     */
    public void playNext() {
        List<Song> list = playlist.getValue();
        Song current = currentSong.getValue();
        if (list == null || current == null) return;
        int idx = indexOf(current, list);
        int nextIdx = (idx < list.size() - 1) ? idx + 1 : 0;
        playSong(list.get(nextIdx));
    }

    /**
     * Play previous song in the current playlist, or wrap if at beginning.
     */
    public void playPrevious() {
        List<Song> list = playlist.getValue();
        Song current = currentSong.getValue();
        if (list == null || current == null) return;
        int idx = indexOf(current, list);
        int prevIdx = (idx > 0) ? idx - 1 : list.size() - 1;
        playSong(list.get(prevIdx));
    }

    private int indexOf(Song target, List<Song> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == target.getId()) {
                return i;
            }
        }
        return -1;
    }

    public LiveData<Song> getCurrentSong() {
        return currentSong;
    }

    public LiveData<List<Song>> getPlaylist() {
        return playlist;
    }
}
