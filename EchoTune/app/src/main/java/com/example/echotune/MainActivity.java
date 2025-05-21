package com.example.echotune;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echotune.ui.CreatePlaylistDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.echotune.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private TextView toolbarTitle;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and bind the layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the custom toolbar
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(findViewById(R.id.toolbar_main));
        // Disable default title display to use custom TextView
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setToolbarTitle("Home");    // Default title on launch

        // Set up BottomNavigationView with Navigation components
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_playlists)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Updates the toolbar title and icon based on the current screen
     * @param title The title to display
     */
    public void setToolbarTitle(String title) {
        if (toolbarTitle != null) {
            // Set title
            toolbarTitle.setText(title);
            // Change icon drawable according to title
            switch (title){
                case "Home": toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home, 0, 0, 0); break;
                case "Discover": toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0); break;
                case "Playlists": toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_name, 0, 0, 0); break;
                case "Profile": toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person, 0, 0, 0); break;
            }
        }
    }

    /**
     * Called when a play button in any song card is clicked
     * @param view The clicked view (button)
     */
    public void play(View view){
        // Retrieve the parent card to get the Song object from its tag
        View parentCard = (View) view.getParent();
        Song song = (Song) parentCard.getTag();

        if (song != null) {
            // Start PlayingActivity, passing the Song and caller info
            Intent intent = new Intent(this, PlayingActivity.class);
            intent.putExtra("song", song);
            intent.putExtra("caller", "single");
            startActivity(intent);
        }
    }

    /**
     * Called when the "Create Playlist" button is clicked
     * @param view The clicked view
     */
    public void createPlaylist(View view){
        // Show the CreatePlaylistDialogFragment
        new CreatePlaylistDialogFragment().show(this.getSupportFragmentManager(), "createPlaylist");
    }
}
