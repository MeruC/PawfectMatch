package com.example.newtry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newtry.DogApiService;
import com.example.newtry.DogBreed;
import com.example.newtry.DogBreedAdapter;
import com.example.newtry.DogDetails;
import com.example.newtry.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class Encyclopediaa extends AppCompatActivity implements DogApiService.DogBreedsCallback, DogBreedAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private DogBreedAdapter adapter;
    private EditText etSearch;
    private List<DogBreed> dogBreeds;
    private List<DogBreed> filteredDogBreeds;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encyclopedia_recycler);

        recyclerView = findViewById(R.id.recycler_view);
        etSearch = findViewById(R.id.etSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DogApiService apiService = new DogApiService();
        apiService.fetchDogBreeds(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim().toLowerCase(Locale.getDefault());
                filterDogBreeds(query); // Call the method to filter the dog breeds based on the search query
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavgationView);
        bottomNavigationView1.setSelectedItemId(R.id.bottom_book);

        bottomNavigationView1.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                return true;
            } else if (item.getItemId() == R.id.bottom_matcher) {
                startActivity(new Intent(getApplicationContext(), startDogMatcher.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.bottom_book) {
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

  @Override
  public void onItemClick(DogBreed dogBreed) {
      if (dogBreed == null) {
          //Toast.makeText(this, "No dog breed selected", Toast.LENGTH_SHORT).show();
      } else {
         // Toast.makeText(this, "Selected dog breed: " + dogBreed.getName(), Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(this, DogDetails.class);
          intent.putExtra("selectedDog", dogBreed);
          startActivity(intent);
      }
  }

    private void filterDogBreeds(String query) {
        filteredDogBreeds.clear();

        if (query.isEmpty()) {
            filteredDogBreeds.addAll(dogBreeds);
        } else {
            for (DogBreed dogBreed : dogBreeds) {
                if (dogBreed.getName().toLowerCase(Locale.getDefault()).startsWith(query.toLowerCase(Locale.getDefault()))) {
                    filteredDogBreeds.add(dogBreed);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onSuccess(final List<DogBreed> dogBreeds) {
        this.dogBreeds = dogBreeds;
        this.filteredDogBreeds = new ArrayList<>(dogBreeds);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new DogBreedAdapter(filteredDogBreeds, Encyclopediaa.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onError(final IOException e) {
        // Update UI on the main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Encyclopediaa.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}