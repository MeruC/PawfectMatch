package com.example.newtry;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.List;

public class DogAlbumActivity extends AppCompatActivity {

    private GridView gridView;
    private FirebaseFirestore db;
    private FirebaseAuth auth; // Declare the auth variable
    private List<Dog> dogList;
    private DogAdapter adapter;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_album);

        // Initialize views and variables
        gridView = findViewById(R.id.gridView);
        dogList = new ArrayList<>();
        adapter = new DogAdapter(this, dogList);
        gridView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance(); // Initialize the auth variable
        String userId = auth.getCurrentUser().getUid();

        // Retrieve dog data from Firestore
        // Retrieve dog data from Firestore
        db.collection("users").document(userId).collection("dogs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("DogAlbumActivity", "Error getting dog collection: ", error);
                            return;
                        }

                        dogList.clear();

                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Dog dog = document.toObject(Dog.class);
                            String dogId = document.getId(); // Get the document ID as the dog ID
                            dog.setId(dogId); // Set the dog ID in the Dog object
                            dogList.add(dog);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });


        // Set click listener for Add Dog button
        Button addDogButton = findViewById(R.id.addDogButton);
        addDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddDogActivity to add a new dog
                Intent intent = new Intent(DogAlbumActivity.this, AddDogActivity.class);
                startActivity(intent);
            }
        });

        // Set item click listener for GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dog dog = dogList.get(position);
                // Open full details activity or fragment for the selected dog
                Intent intent = new Intent(DogAlbumActivity.this, DogDetailsActivity.class);
                intent.putExtra("dog", dog);
                startActivity(intent);
            }
        });

        // Set item long click listener for GridView
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Dog dog = dogList.get(position);
                showOptionsDialog(dog);
                return true;
            }
        });


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavgationView);
        bottomNavigationView1.setSelectedItemId(R.id.bottom_random);


        bottomNavigationView1.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.bottom_logout) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                return false;
            }
            return true;
        });
    }

    private void showOptionsDialog(final Dog dog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Open EditDogActivity to edit the selected dog

                            // Open EditDogActivity to edit the selected dog
                            Intent intent = new Intent(DogAlbumActivity.this, EditDogActivity.class);
                            intent.putExtra("dog", dog);
                            intent.putExtra("dogId", dog.getId()); // Pass the dog ID

                            startActivity(intent);
                        } else if (which == 1) {
                            //Delete option selected, delete the dog
                            deleteDog(dog);
                        }
                    }
                });

        builder.create().show();
    }


    private void deleteDog(final Dog dog) {
        // Create a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this dog?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the dog from Firestore
                        deleteDogFromFirestore(dog);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteDogFromFirestore(Dog dog) {
        // Delete the dog from Firestore
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("dogs")
                .document(dog.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DogAlbumActivity.this, "Dog deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DogAlbumActivity.this, "Error deleting dog", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

}
