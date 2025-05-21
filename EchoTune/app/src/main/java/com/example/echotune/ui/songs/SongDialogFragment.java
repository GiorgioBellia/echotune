package com.example.echotune.ui.songs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.echotune.R;
import com.example.echotune.SongAdapter;
import com.example.echotune.ui.discover.SongsViewModel;
import java.util.ArrayList;

public class SongDialogFragment extends DialogFragment {
    private SongsViewModel songsViewModel;
    private SongAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Initialize ViewModel
        songsViewModel = new ViewModelProvider(requireActivity()).get(SongsViewModel.class);

        // Inflate the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.songs_layout, null);

        TextView title = dialogView.findViewById(R.id.dialog_title);

        // Create an AlertDialog.Builder to build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Initialize RecyclerView to display the songs
        RecyclerView recyclerView = dialogView.findViewById(R.id.songs_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the tag (purpose) of the dialog to determine whether to add or remove songs
        String purpose = getTag();

        // Handle the dialog for adding a song
        if (purpose.equals("addSongDialog")){
            title.setText("Add Songs");
            // Initialize the adapter with adapterType 1 which is the one to add songs
            adapter = new SongAdapter(new ArrayList<>(), 1);
            recyclerView.setAdapter(adapter);

            // Observe the LiveData for songs not in the playlist, and update the adapter with the new songs data
            songsViewModel.getNonPlaylistSongs().observe(this, songs -> {
                if (songs != null) {
                    adapter.updateSongs(songs);
                }
            });
        }
        // Handle the dialog for removing a song
        else if (purpose.equals("removeSongDialog")){
            title.setText("Remove Songs");
            // Initialize the adapter with adapterType 2 which is the one to remove songs
            adapter = new SongAdapter(new ArrayList<>(), 2);
            recyclerView.setAdapter(adapter);

            // Observe the LiveData for songs in the playlist, and update the adapter with the new songs data
            songsViewModel.getPlaylistSongs().observe(this, songs -> {
                if (songs != null) {
                    adapter.updateSongs(songs);
                }
            });
        }

        return dialog;
    }
}
