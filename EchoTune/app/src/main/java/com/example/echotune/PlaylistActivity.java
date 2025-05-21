package com.example.echotune;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.echotune.backend.PlaylistSongsDbHelper;
import com.example.echotune.databinding.ActivityPlaylistBinding;
import com.example.echotune.ui.songs.SongDialogFragment;
import com.example.echotune.ui.discover.SongsViewModel;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private ActivityPlaylistBinding binding;
    private ImageButton backButton;
    private TextView title;
    private String playlist;
    private RecyclerView recyclerViewSongs;
    private SongAdapter adapterSongs;
    private PlaylistViewModel playlistViewModel;
    private SongsViewModel songsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel scoped to this Activity
        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        // Inflate layout using view binding
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up custom toolbar without default title
        setSupportActionBar(findViewById(R.id.toolbar_playlist));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Configure back button to navigate back
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        // Set playlist title from Intent extra
        title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("Title"));
        playlist = (String) title.getText();

        // Pass the playlist to the viewmodel
        playlistViewModel.setPlaylist(playlist);

        // Setup RecyclerView for playlist songs
        recyclerViewSongs = findViewById(R.id.song_list);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        List<Song> playlistList = new ArrayList<>();
        adapterSongs = new SongAdapter(playlistList, 0);
        recyclerViewSongs.setAdapter(adapterSongs);

        // Load initial songs for this playlist from DB and display
        playlistViewModel.getSongsLiveData().observe(this, songs -> {
            adapterSongs.updateSongs(songs);
        });
    }

    /**
     * Open dialog to add a new song to this playlist.
     */
    public void addSongDialog(View view){
        SongDialogFragment dialogFragment = new SongDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "addSongDialog");
        // Load songs not already in this playlist
        songsViewModel.loadNonPlaylistSongs(playlist);
    }

    /**
     * Open dialog to remove a song from this playlist.
     */
    public void removeSongDialog(View view){
        SongDialogFragment dialogFragment = new SongDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "removeSongDialog");
        // Load songs currently in this playlist
        songsViewModel.loadPlaylistSongs(playlist);
    }

    /**
     * Play a selected song from the playlist.
     */
    public void play(View view){
        View parentCard = (View) view.getParent();
        Song song = (Song) parentCard.getTag();

        if (song != null) {
            // Start PlayingActivity with playlist context
            Intent intent = new Intent(this, PlayingActivity.class);
            intent.putExtra("song", song);
            intent.putExtra("playlist", playlist);
            // Pass playlist intent
            intent.putExtra("caller", "playlist");
            startActivity(intent);
        }
    }

    /**
     * Add the selected song to this playlist in the DB and update UI.
     */
    public void add(View view){
        View parentCard = (View) view.getParent();
        Song song = (Song) parentCard.getTag();

        int song_id = (int) song.getId();
        String playlist = (String) title.getText();

        if(playlistViewModel.addSong(playlist, song_id)){
            Toast.makeText(this, "Song added to "+playlist, Toast.LENGTH_SHORT).show();
            playlistViewModel.getSongsLiveData().observe(this, songs -> {
                adapterSongs.updateSongs(songs);
            });
        }else{
            Toast.makeText(this, playlist+" already contains this song", Toast.LENGTH_SHORT).show();
        }
        songsViewModel.loadNonPlaylistSongs(playlist);
    }

    /**
     * Remove the selected song from this playlist in the DB and update UI.
     */
    public void remove(View view){
        View parentCard = (View) view.getParent();
        Song song = (Song) parentCard.getTag();

        int song_id = (int) song.getId();
        String playlist = (String) title.getText();

        if(playlistViewModel.removeSong(playlist, song_id)){
            Toast.makeText(this, "Song removed from "+playlist, Toast.LENGTH_SHORT).show();
            playlistViewModel.getSongsLiveData().observe(this, songs -> {
                adapterSongs.updateSongs(songs);
            });
        }else{
            Toast.makeText(this, playlist+" does not contain this song", Toast.LENGTH_SHORT).show();
        }
        songsViewModel.loadPlaylistSongs(playlist);
    }
}
