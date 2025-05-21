package com.example.echotune;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.echotune.backend.PlaylistSongsDbHelper;
import com.example.echotune.backend.SongDbHelper;
import com.example.echotune.databinding.ActivityPlayingBinding;
import java.util.ArrayList;
import java.util.List;

public class PlayingActivity extends AppCompatActivity {
    private ActivityPlayingBinding binding;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable updateSeekBar;
    private Song song;
    private String playlist;
    private List<Song> playlistSongs;
    private PlayingViewModel playingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize handler tied to main thread
        handler = new Handler(Looper.getMainLooper());

        // Inflate layout using view binding
        binding = ActivityPlayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up custom toolbar
        setSupportActionBar(findViewById(R.id.toolbar_playing));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize the viewmodel
        playingViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(PlayingViewModel.class);

        // Get Song object passed via Intent
        song = (Song) getIntent().getSerializableExtra("song");

        // Set initial song title, author, and play count in UI
        TextView titleText = findViewById(R.id.title);
        TextView authorText = findViewById(R.id.author);
        TextView playCount = findViewById(R.id.play_count);
        titleText.setText(song.getTitle());
        authorText.setText(song.getAuthor());
        playCount.setText("Played: "+song.getReplays());

        // Initialize SeekBar and its logic
        seekBar = findViewById(R.id.seek_bar);
        setupSeekBar();

        // Determine playback context: single song or playlist
        String caller = getIntent().getStringExtra("caller");
        if (caller.equals("single")) {
            // Playing from all songs list
            playingViewModel.setPlaylist(null);
        } else {
            // Playing from a specific playlist
            playlist = getIntent().getStringExtra("playlist");
            playingViewModel.setPlaylist(playlist);
        }

        // Observe currentSong LiveData
        playingViewModel.getPlaylist().observe(this, playlist -> {
            if (playlist == null) return;
            playlistSongs = playlist;
        });

        // Observe currentSong LiveData
        playingViewModel.getCurrentSong().observe(this, newSong -> {
            if (newSong == null) return;
            updateUIWithSong(newSong);
            startMediaPlayerForSong(newSong);
        });

        // Start playback of initial selection
        playingViewModel.playSong(song);

        // Handle Android back button to stop and release MediaPlayer
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    // Update UI elements when switching songs
    private void updateUIWithSong(Song newSong) {
        TextView titleText = findViewById(R.id.title);
        TextView authorText = findViewById(R.id.author);
        TextView playCount = findViewById(R.id.play_count);
        titleText.setText(newSong.getTitle());
        authorText.setText(newSong.getAuthor());
        playCount.setText("Played: "+newSong.getReplays());
    }

    // Initialize and start MediaPlayer for the given song
    private void startMediaPlayerForSong(Song newSong) {
        // Release old player if exists
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Determine resource ID
        String songName = "song" + newSong.getId();
        int resId = getResources().getIdentifier(songName, "raw", getPackageName());
        if (resId == 0) {
            Toast.makeText(this, "Song file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and prepare MediaPlayer
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.setOnPreparedListener(mp -> {
            // Set SeekBar max to song duration and start playback
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            handler.post(updateSeekBar);
        });
        // Set action when the seekbar completes
        mediaPlayer.setOnCompletionListener(mp -> {
            playingViewModel.playNext();
        });
    }

    // Configure SeekBar behavior and update logic
    private void setupSeekBar() {
        // Handle user dragging the SeekBar thumb
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean userTouch = false;
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (userTouch && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userTouch = false;
            }
        });

        // Runnable for updating progress
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            }
        };
    }

    // Handle toolbar back button click
    public void back(View view) {
        getOnBackPressedDispatcher().onBackPressed();
    }

    // Toggle play/pause state when play_stop button clicked
    public void play_stop(View view) {
        ImageButton button = findViewById(R.id.play_stop);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            button.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            button.setImageResource(R.drawable.pause);
        }
    }

    // Play previous song in playlist
    public void previous(View view) {
        playingViewModel.playPrevious();
    }

    // Play next song in playlist
    public void next(View view) {
        playingViewModel.playNext();
    }
}
