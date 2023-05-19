package com.example.newtry;



import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DogApiService {
    private static final String DOG_API_URL = "https://api.thedogapi.com/v1/breeds";
    public void fetchDogBreeds(final DogBreedsCallback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(DOG_API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        List<DogBreed> dogBreeds = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DogBreed dogBreed = new DogBreed();

                            dogBreed.setId(jsonObject.getInt("id"));
                            dogBreed.setName(jsonObject.getString("name"));
                            dogBreed.setBredFor(jsonObject.optString("bred_for"));

                            JSONObject weightObject = jsonObject.getJSONObject("weight");
                            dogBreed.setWeight(weightObject.getString("imperial"));

                            JSONObject heightObject = jsonObject.getJSONObject("height");
                            dogBreed.setHeight(heightObject.getString("imperial"));

                            dogBreed.setLifeSpan(jsonObject.optString("life_span"));
                            dogBreed.setBreedGroup(jsonObject.optString("breed_group"));
                            dogBreed.setTemperament(jsonObject.optString("temperament"));

                            // Fetch the image URL
                            JSONObject imageObject = jsonObject.optJSONObject("image");
                            if (imageObject != null) {
                                String imageUrl = imageObject.optString("url");
                                dogBreed.setImageUrl(imageUrl);
                                Log.d("ImageUrl", "Value: " + imageUrl);
                            }

                            dogBreeds.add(dogBreed);
                        }

                        // Callback with the fetched dog breeds
                        callback.onSuccess(dogBreeds);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError(new IOException("Failed to parse JSON response"));
                    }
                } else {
                    onError(new IOException("Failed to fetch dog breeds: " + response.code()));
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                onError(e);
            }
            private void onError(final IOException e) {
                // Update UI on the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(e);
                    }
                });
            }
        });
    }
    public interface DogBreedsCallback {
        void onSuccess(List<DogBreed> dogBreeds);
        void onError(IOException e);
    }
}

