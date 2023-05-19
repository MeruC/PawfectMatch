package com.example.newtry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Home extends AppCompatActivity {
    private static final String API_ENDPOINT = "https://api.thedogapi.com/v1/breeds/";
    private ImageView dogImageView;
    private TextView breedNameTextView;
    private TextView bredForTextView;
    private TextView breedGroupTextView;
    private TextView lifeSpanTextView;
    private TextView temperamentTextView;
    Button btnDownload;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initialize();

        initializeViews();

        fetchRandomDogBreed();

    }

    private void initialize() {


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavgationView);
        bottomNavigationView1.setSelectedItemId(R.id.bottom_home);


        bottomNavigationView1.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_matcher) {
                startActivity(new Intent(getApplicationContext(), startDogMatcher.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.bottom_book) {
                startActivity(new Intent(getApplicationContext(), Encyclopediaa.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.bottom_random) {
                startActivity(new Intent(getApplicationContext(), DogAlbumActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.bottom_logout) {
                showLogoutConfirmationDialog();
                return true;
            } else {
                return false;
            }
        });
    }

    private void initializeViews() {
        dogImageView = findViewById(R.id.dog_image);
        breedNameTextView = findViewById(R.id.breed_name_text);
        bredForTextView = findViewById(R.id.bred_for_text);
        breedGroupTextView = findViewById(R.id.breed_group_text);
        lifeSpanTextView = findViewById(R.id.life_span_text);
        temperamentTextView = findViewById(R.id.temperament_text);


        //hide contents
        FrameLayout contents = findViewById(R.id.contents);
        contents.setVisibility(View.GONE);
        BottomNavigationView bottomNavgationView = findViewById(R.id.bottomNavgationView);
        bottomNavgationView.setVisibility(View.GONE);


        //Show contents
        //FrameLayout animationContainer = findViewById(R.id.animationContainer);
        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.setAnimation("dogwalkingg.json");
        animationView.playAnimation();

        // Stop the animation after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);

                contents.setVisibility(View.VISIBLE);
                bottomNavgationView.setVisibility(View.VISIBLE);

            }
        }, 5000);


    }

    private void fetchRandomDogBreed() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String json = responseBody.string();
                        try {
                            JSONArray jsonArray = new JSONArray(json);

                            int maxId = jsonArray.length(); // Get the maximum ID available

                            int randomId = generateRandomId(maxId); // Generate a random ID within the range

                            JSONObject jsonObject = jsonArray.getJSONObject(randomId);

                            String breedName = jsonObject.getString("name");
                            String bredFor = jsonObject.getString("bred_for");
                            String breedGroup = jsonObject.getString("breed_group");
                            String lifeSpan = jsonObject.getString("life_span");
                            String temperament = jsonObject.getString("temperament");
                            String imageUrl = jsonObject.getString("reference_image_id");

                            btnDownload = findViewById(R.id.btnDownload);
                            btnDownload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadImage("https://cdn2.thedogapi.com/images/" + imageUrl + ".jpg");
                                }
                            });

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    breedNameTextView.setText(breedName);
                                    bredForTextView.setText("Bred for: " + bredFor);
                                    breedGroupTextView.setText("Breed Group: " + breedGroup);
                                    lifeSpanTextView.setText("Lifespan: " + lifeSpan);
                                    temperamentTextView.setText("Character: " + temperament);

                                    Picasso.get().load("https://cdn2.thedogapi.com/images/" + imageUrl + ".jpg")
                                            .into(dogImageView);
                                }

                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    // Handle the case when the request is not successful
                }
            }
        });


    }

    private int generateRandomId(int maxId) {
        Random random = new Random();
        return random.nextInt(maxId);
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

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setCancelable(false);
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                redirectToLogin();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
