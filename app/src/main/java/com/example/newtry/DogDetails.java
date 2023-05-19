package com.example.newtry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DogDetails extends AppCompatActivity {
    private TextView tvDogName;
    private TextView tvDogBreedFor, tvDogHeight, tvDogWeight, tvDogGroup, tvDogTemperament, tvDogLifeSpan;

    ImageView ImDogImage;

    Button btnDownload, btnBack;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_details);

        btnBack = findViewById(R.id.btnBack);
        btnDownload = findViewById(R.id.btnDownload);
        tvDogName = findViewById(R.id.tvDogName);
        tvDogBreedFor= findViewById(R.id.tvDogBreedFor);
        tvDogHeight= findViewById(R.id.tvDogHeight);
        tvDogWeight= findViewById(R.id.tvDogWeight);
        tvDogGroup= findViewById(R.id.tvDogGroup);
        tvDogTemperament= findViewById(R.id.tvDogTemperament);
        tvDogLifeSpan= findViewById(R.id.tvDogLifeSpan);
        ImDogImage= findViewById(R.id.ImDogImage);

        // Retrieve the selected dog object from the intent
        DogBreed selectedDog = (DogBreed) getIntent().getSerializableExtra("selectedDog");

        // Display the dog's data in the appropriate views
        if (selectedDog != null) {
            String url_image = selectedDog.getImageUrl();
            tvDogName.setText("Breed Name: "+selectedDog.getName());
            tvDogBreedFor.setText("Bred for: "+selectedDog.getBredFor());
            tvDogHeight.setText("Height: "+selectedDog.getHeight()+" inches");
            tvDogWeight.setText("Weight: "+selectedDog.getWeight()+" pounds");
            tvDogGroup.setText("Breed Group: "+selectedDog.getBreedGroup());
            tvDogTemperament.setText("Character: "+selectedDog.getTemperament());
            tvDogLifeSpan.setText("Lifespan: "+selectedDog.getLifeSpan());

            String dogImageUrl =  "https://cdn2.thedogapi.com/images/" + url_image + ".jpg";
            Picasso.get().load(url_image).into(ImDogImage);

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadImage(url_image);
                }
            });

        }
        else{
            tvDogName.setText("walang laman");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void downloadImage(String imageUrl) {
        Target imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveImageToGallery(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(getApplicationContext(), "Failed to download image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.get().load(imageUrl).into(imageTarget);
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String filename = "image_" + System.currentTimeMillis() + ".jpg";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            // Add the image to the gallery
            Uri imageUri = Uri.fromFile(file);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri);
            sendBroadcast(mediaScanIntent);

            Toast.makeText(this, "Image downloaded", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}
