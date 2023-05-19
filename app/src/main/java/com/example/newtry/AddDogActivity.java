package com.example.newtry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddDogActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText;
    private EditText breedEditText;
    private EditText ageEditText;
    private Button saveButton;
    private ImageView pictureImageView;
    private Uri pictureUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_add_dog);



        nameEditText = findViewById(R.id.nameEditText);
        breedEditText = findViewById(R.id.breedEditText);
        ageEditText = findViewById(R.id.ageEditText);
        saveButton = findViewById(R.id.saveButton);
        pictureImageView = findViewById(R.id.pictureImageView);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDog();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pictureUri = data.getData();
            pictureImageView.setImageURI(pictureUri);
        }
    }

    private void saveDog() {
        String name = nameEditText.getText().toString().trim();
        String breed = breedEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();

        // Perform validation for name, breed, and age
        if (name.isEmpty()) {
            nameEditText.setError("Please enter a name");
            return;
        }

        if (breed.isEmpty()) {
            breedEditText.setError("Please enter a breed");
            return;
        }

        if (age.isEmpty()) {
            ageEditText.setError("Please enter an age");
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        // Create a new Dog object with the entered details
        String id = db.collection("users").document().getId(); // Generate a unique ID
        Dog dog = new Dog(id, null, name, breed, age); // Set pictureUrl as null for now

        // Save the dog to Firestore
        db.collection("users").document(userId).collection("dogs")
                .add(dog)
                .addOnSuccessListener(documentReference -> {
                    String dogId = documentReference.getId();
                    String imageFileName = "dogs/" + dogId + ".jpg"; // Set the image file name

                    // Upload the image to Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageFileName);
                    storageRef.putFile(pictureUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Get the download URL of the uploaded image
                                storageRef.getDownloadUrl()
                                        .addOnSuccessListener(uri -> {
                                            String pictureUrl = uri.toString();

                                            // Update the dog document in Firestore with the pictureUrl
                                            Map<String, Object> updates = new HashMap<>();
                                            updates.put("picture", pictureUrl);

                                            db.collection("users")
                                                    .document(userId)
                                                    .collection("dogs")
                                                    .document(dogId)
                                                    .update(updates)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(AddDogActivity.this, "Dog saved successfully", Toast.LENGTH_SHORT).show();
                                                        finish(); // Finish the activity and go back to DogAlbumActivity
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(AddDogActivity.this, "Error saving dog", Toast.LENGTH_SHORT).show();
                                                        e.printStackTrace();
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(AddDogActivity.this, "Error getting image URL", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddDogActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddDogActivity.this, "Error saving dog", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }


}
