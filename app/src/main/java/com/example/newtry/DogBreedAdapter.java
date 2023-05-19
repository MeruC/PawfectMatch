package com.example.newtry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DogBreedAdapter extends RecyclerView.Adapter<DogBreedAdapter.ViewHolder> {
    private List<DogBreed> dogBreeds;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(DogBreed dogBreed);
    }
    public DogBreedAdapter(List<DogBreed> dogBreeds, OnItemClickListener listener) {
        this.dogBreeds = dogBreeds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog_breed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DogBreed dogBreed = dogBreeds.get(position);
        holder.bind(dogBreed);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(dogBreed);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dogBreeds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView breedNameTextView;
        private ImageView dogImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            breedNameTextView = itemView.findViewById(R.id.text_breed_name);
            dogImage = itemView.findViewById(R.id.dogImage);
        }
        public void bind(DogBreed dogBreed) {
            breedNameTextView.setText(dogBreed.getName());

            String imageUrl = dogBreed.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(dogImage);
            } else {
                // Set a placeholder image or handle the case when the image URL is not available
            }
        }
    }
}
