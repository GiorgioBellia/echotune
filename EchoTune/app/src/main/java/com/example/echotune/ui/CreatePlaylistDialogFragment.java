package com.example.echotune.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.echotune.R;
import com.example.echotune.ui.playlists.PlaylistsViewModel;

public class CreatePlaylistDialogFragment extends DialogFragment {
    private PlaylistsViewModel playlistsViewModel;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the ViewModel to update the UI after creating a new playlist
        playlistsViewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(PlaylistsViewModel.class);

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_playlist_layout, null);
        // Get reference to the EditText where user types playlist name
        EditText input = dialogView.findViewById(R.id.playlist_name_input);

        // Build dialog
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        // Set create button action
        dialog.setOnShowListener(d -> {
            Button createButton = dialogView.findViewById(R.id.create_button);
            createButton.setOnClickListener(v -> {
                String name = input.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
                    new CreatePlaylistDialogFragment().show(getParentFragmentManager(), "createPlaylist");
                    dialog.dismiss();
                } else {
                    if (playlistsViewModel.createPlaylist(name)) {
                        Toast.makeText(getContext(), "Playlist created", Toast.LENGTH_SHORT).show();
                        playlistsViewModel.loadPlaylists();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Playlist name already existing", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        return dialog;
    }
}
