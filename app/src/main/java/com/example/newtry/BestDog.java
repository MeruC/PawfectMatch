package com.example.newtry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BestDog extends AppCompatActivity {
    ImageView ImDog;
    TextView tvDogName, tvMinLife, tvMaxLife, tvMinWeight, tvMaxWeight, tvMinHeight, tvMaxHeight;
    Slider slChildren, slODogs, slPlayful, slProtective, slTrain, slBark;
    Button btnOk, btnDownload;

    @Override
    public void onBackPressed() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_dog);

        initialize();
    }

    private void initialize() {

        tvDogName = findViewById(R.id.tvDogName);
        tvMinLife = findViewById(R.id.tvMinLife);

        tvMinWeight= findViewById(R.id.tvMinWeight);

        tvMinHeight= findViewById(R.id.tvMinHeight);


        slChildren= findViewById(R.id.slChildren);
        slODogs= findViewById(R.id.slDog);
        slPlayful= findViewById(R.id. slPlayful);
        slProtective= findViewById(R.id.slProtective);
        slTrain= findViewById(R.id. slTrain);
        slBark = findViewById(R.id.slBark); ;

        ImDog = findViewById(R.id.ImDog);
        btnOk = findViewById(R.id.btnOk);

        //hide contents
        LinearLayout contents = findViewById(R.id.contents);
        contents.setVisibility(View.GONE);

         // Get the intent that started this activity
        Intent intent1 = getIntent();

        // Extract the data from the intent
        String name = intent1.getStringExtra("name");
        String minLife = intent1.getStringExtra("min_life");

        String minWeight = intent1.getStringExtra("min_weight");

        String minHeight = intent1.getStringExtra("min_height");

        String imageUrl = intent1.getStringExtra("image_url");

        String gChildren = intent1.getStringExtra("gChildren");
        String gDogs = intent1.getStringExtra("gDogs");
        String playful = intent1.getStringExtra("playful");
        String protective = intent1.getStringExtra("protective");
        String train = intent1.getStringExtra("train");
        String barking = intent1.getStringExtra("barking");

        slChildren.setValue(Integer.parseInt(gChildren));
        slODogs.setValue(Integer.parseInt(gDogs));
        slPlayful.setValue(Integer.parseInt(playful));
        slProtective.setValue(Integer.parseInt(protective));
        slTrain.setValue(Integer.parseInt(train));
        slBark.setValue(Integer.parseInt(barking));

        tvDogName.setText("Name: "+name);
        tvMinLife.setText("Minimum Life Span: "+minLife.replaceAll("\\.0$", "")+" years");

        tvMinWeight.setText("Minimum Weight: "+minWeight.replaceAll("\\.0$", "")+" pounds");

        tvMinHeight.setText("Minimum Height: "+minHeight.replaceAll("\\.0$", "")+" inches");

        // Load the dog image using Picasso library
        Picasso.get().load(imageUrl)
                .into(ImDog);


        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(imageUrl);
            }
        });



        //Show contents
        FrameLayout animationContainer = findViewById(R.id.animationContainer);
        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.setAnimation("cutedog.json");
        animationView.playAnimation();

        // Stop the animation after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);

                contents.setVisibility(View.VISIBLE);

            }
        }, 5000);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to Home
                Intent intent = new Intent(BestDog.this, Home.class);
                startActivity(intent);
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