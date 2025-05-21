package com.example.echotune.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.echotune.R;
import com.example.echotune.MainActivity;
import com.example.echotune.Song;
import com.example.echotune.databinding.FragmentHomeBinding;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the ViewModel for this fragment
        homeViewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(HomeViewModel.class);

        // Inflate the layout for the fragment and get the root view
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set the toolbar title
        ((MainActivity) getActivity()).setToolbarTitle("Home");

        // Observe the LiveData for songs, and update the UI when the data changes
        homeViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                updateSongCards(songs);
            }
        });

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
        // Reload the list of songs whenever the fragment is resumed
        homeViewModel.loadSongs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Updates the song cards in the UI with the given list of home songs
     */
    public void updateSongCards(List<Song> homeSongs){
        int[] cardIds = {
                R.id.song_card_1,
                R.id.song_card_2,
                R.id.song_card_3,
                R.id.song_card_4,
                R.id.song_card_5,
                R.id.song_card_6
        };

        // Iterate over each card and update its contents with song data
        for (int i=0; i < cardIds.length; i++){
            RelativeLayout cardLayout = binding.getRoot().findViewById(cardIds[i]);
            TextView title = cardLayout.findViewById(R.id.text_title);
            TextView author = cardLayout.findViewById(R.id.text_author);

            Song song;
            try {
                song = homeSongs.get(i);
            }catch (Exception e){
                song = null;
            }

            if (song != null) {
                title.setText(song.getTitle());
                author.setText(song.getAuthor());
                cardLayout.setTag(song);
            }
        }
    }
}
