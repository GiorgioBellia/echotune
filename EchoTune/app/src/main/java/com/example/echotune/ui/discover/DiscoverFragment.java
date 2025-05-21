package com.example.echotune.ui.discover;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.echotune.GenreAdapter;
import com.example.echotune.MainActivity;
import com.example.echotune.R;
import com.example.echotune.Song;
import com.example.echotune.SongAdapter;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    private DiscoverViewModel discoverViewModel;
    private RecyclerView recyclerViewSongs;
    private RecyclerView recyclerViewGenres;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the ViewModel for this fragment
        discoverViewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(DiscoverViewModel.class);
        // Inflate the fragment layout
        View root = inflater.inflate(R.layout.fragment_discover, container, false);

        // Set toolbar title
        ((MainActivity) getActivity()).setToolbarTitle("Discover");

        // Setup horizontal RecyclerView for genres
        recyclerViewGenres = root.findViewById(R.id.genre_list);
        recyclerViewGenres.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<String> genreList = new ArrayList<>();

        // Initialize genre adapter with click listener
        GenreAdapter adapterGenres = new GenreAdapter(genreList, genreButton -> {
            handleGenreSelection(genreButton);
        });
        recyclerViewGenres.setAdapter(adapterGenres);

        // Observe genreList from ViewModel and update adapter
        discoverViewModel.getAllGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                adapterGenres.updateGenre(genres);
            }
        });

        // Setup vertical RecyclerView for songs
        recyclerViewSongs = root.findViewById(R.id.song_list);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Song> songList = new ArrayList<>();

        // Initialize song adapter with adapterType to 0 which is the one with the playing button
        SongAdapter adapterSongs = new SongAdapter(songList, 0);
        recyclerViewSongs.setAdapter(adapterSongs);

        // Observe song list from ViewModel and update adapter
        discoverViewModel.getAllSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                adapterSongs.updateSongs(songs);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Clear genre selection and show all songs when returning to this fragment
        clearGenreSelection();
    }

    /**
     * Handles when a genre button is clicked
     * Toggles selection state and updates song list accordingly
     */
    private void handleGenreSelection(MaterialButton selectedButton) {
        // Loop through all genre buttons
        for (int i = 0; i < recyclerViewGenres.getChildCount(); i++) {
            View itemView = recyclerViewGenres.getChildAt(i);
            MaterialButton button = itemView.findViewById(R.id.genre_button);
            // Deselect all buttons except the selected one
            if (button != null && button != selectedButton) {
                button.setBackgroundColor(getResources().getColor(R.color.primary));
                button.setTag(false);
            }
        }

        // Toggle current button's selection state
        Object tag = selectedButton.getTag();
        boolean isSelected = tag != null && (boolean) tag;

        if (isSelected) {
            // Deselect: reset button color and load all songs
            selectedButton.setBackgroundColor(getResources().getColor(R.color.primary));
            selectedButton.setTag(false);
            discoverViewModel.loadAllSongs();
        } else {
            // Select: highlight button and filter songs by genre
            selectedButton.setBackgroundColor(getResources().getColor(R.color.tertiary));
            selectedButton.setTag(true);
            discoverViewModel.loadSongsByGenre(selectedButton.getText().toString());
        }
    }

    /**
     * Deselects all genre buttons and loads all songs
     */
    private void clearGenreSelection() {
        // Loop through all genre buttons
        for (int i = 0; i < recyclerViewGenres.getChildCount(); i++) {
            View itemView = recyclerViewGenres.getChildAt(i);
            MaterialButton button = itemView.findViewById(R.id.genre_button);
            // Deselect all buttons
            if (button != null) {
                button.setBackgroundColor(getResources().getColor(R.color.primary));
                button.setTag(false);
            }
        }

        // Reload full song list
        discoverViewModel.loadAllSongs();
    }
}
