package com.example.echotune;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

// Adapter for displaying a list of genres as buttons in a RecyclerView
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.genreViewHolder> {
    private List<String> genreList;
    private OnGenreClickListener listener;

    public GenreAdapter(List<String> genreList, OnGenreClickListener listener) {
        this.genreList = genreList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public genreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_card, parent, false);
        return new genreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull genreViewHolder holder, int position) {
        // Get the genre name for this position
        String genre = genreList.get(position);
        // Set the button text to the genre name
        holder.genreButton.setText(genre);

        // Set up click listener on the button
        holder.genreButton.setOnClickListener(v -> {
            if (listener != null && v instanceof MaterialButton) {
                listener.onGenreClick((MaterialButton) v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    // Update the adapter's data and refresh the RecyclerView
    public void updateGenre(List<String> newGenre) {
        this.genreList = newGenre;
        notifyDataSetChanged();
    }

    // ViewHolder class to hold and recycle genre button views
    static class genreViewHolder extends RecyclerView.ViewHolder {
        MaterialButton genreButton;

        public genreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreButton = itemView.findViewById(R.id.genre_button);
        }
    }

    // Interface for handling genre button click events
    public interface OnGenreClickListener {
        void onGenreClick(MaterialButton genreButton);
    }
}
