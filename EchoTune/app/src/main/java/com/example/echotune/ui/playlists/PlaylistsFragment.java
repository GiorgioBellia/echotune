package com.example.echotune.ui.playlists;

import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.echotune.MainActivity;
import com.example.echotune.PlaylistActivity;
import com.example.echotune.PlaylistAdapter;
import com.example.echotune.R;
import com.example.echotune.databinding.FragmentPlaylistsBinding;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends Fragment implements PlaylistAdapter.OnPlaylistClickListener{
    private FragmentPlaylistsBinding binding;
    private PlaylistsViewModel playlistsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize ViewModel using ViewModelProvider
        playlistsViewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(PlaylistsViewModel.class);

        // Initialize View binding for the fragment layout
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set the toolbar title
        ((MainActivity) getActivity()).setToolbarTitle("Playlists");

        // Initialize RecyclerView to display the playlists
        RecyclerView recyclerView = root.findViewById(R.id.playlist_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> playlistList = new ArrayList<>();

        // Initialize the adapter and set the click listener
        PlaylistAdapter adapter = new PlaylistAdapter(playlistList, this);
        recyclerView.setAdapter(adapter);

        // Observe the LiveData for playlists from the ViewModel, and update the adapter with the new playlists data
        playlistsViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                adapter.updatePlaylists(playlists);
            }
        });

        return root;
    }

    // This method is triggered when a playlist is clicked
    @Override
    public void onPlaylistClick(String playlist) {
        // Create an Intent to open PlaylistActivity
        Intent intent = new Intent(getActivity(), PlaylistActivity.class);
        // Pass the selected playlist title to the activity
        intent.putExtra("Title", playlist);
        // Start PlaylistActivity
        startActivity(intent);
    }
}
