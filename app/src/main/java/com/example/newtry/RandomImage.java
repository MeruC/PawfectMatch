package com.example.newtry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RandomImage extends AppCompatActivity {

    private ImageView Imrandom;
    private Button btnDownload, btnGenerate;

    private OkHttpClient client;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_image);

        Imrandom = findViewById(R.id.Imrandom);
        btnDownload = findViewById(R.id.btnDownload);
        btnGenerate = findViewById(R.id.btnGenerate);

        client = new OkHttpClient();

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomDogImage();
            }
        });
        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavgationView);
        bottomNavigationView1.setSelectedItemId(R.id.bottom_random);

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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), regActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    private void getRandomDogImage() {
        String url = "https://api.thedogapi.com/v1/images/search?format=json&limit=1";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", "live_xCb7rxFkPpsp3AO5od2Ft8YRmpcklABORmdOdwKtEqREkruJlOYSNZi3dJsPzFb8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String json = responseBody.string();
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            if (jsonArray.length() > 0) {
                                final JSONObject dogObject = jsonArray.getJSONObject(0);
                                final String imageUrl = dogObject.getString("url");

                                runOnUiThread(() -> {
                                    loadImage(imageUrl);
                                    btnDownload.setEnabled(true);
                                    // Call the downloadImage() method here passing the imageUrl
                                    btnDownload.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            downloadImage(imageUrl);
                                        }
                                    });

                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(Imrandom);
    }

    public void onDownloadButtonClick(View view) {
        if (hasWritePermission()) {
            // The download is now triggered in the getRandomDogImage() method
        } else {
            requestWritePermission();
        }
    }

    private boolean hasWritePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestWritePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Write permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadImage(String imageUrl) {
        Target imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveImageToGallery(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(RandomImage.this, "Failed to download image", Toast.LENGTH_SHORT).show();
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
        }   catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}