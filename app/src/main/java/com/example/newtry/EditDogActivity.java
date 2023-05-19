package com.example.newtry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditDogActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText breedEditText;
    private EditText ageEditText;
    private Button updateButton;
    private ImageView pictureImageView;
    private Dog dog;
    private String dogId;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private boolean imageChanged = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri pictureUri;
    private StorageReference storageRef;

    Button btnBack;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dog);

        nameEditText = findViewById(R.id.nameEditText);
        breedEditText = findViewById(R.id.breedEditText);
        ageEditText = findViewById(R.id.ageEditText);
        updateButton = findViewById(R.id.updateButton);
        pictureImageView = findViewById(R.id.pictureImageView);
        btnBack = findViewById(R.id.btnBack);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra("dog")) {
            dog = getIntent().getParcelableExtra("dog");
            dogId = getIntent().getStringExtra("dogId");
            if (dog != null) {
                dog.setId(dogId);
                nameEditText.setText(dog.getName());
                breedEditText.setText(dog.getBreed());
                ageEditText.setText(dog.getAge());
                Picasso.get().load(dog.getPicture()).into(pictureImageView);
            }
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDog();
            }
        });

        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateDog() {
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

        if (dog == null || dog.getId() == null) {
            Toast.makeText(EditDogActivity.this, "Invalid dog data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a HashMap to hold the updated fields
        Map<String, Object> updates = new HashMap<>();

        // Check if the name has changed and update it
        if (!name.equals(dog.getName())) {
            updates.put("name", name);
        }

        if (!breed.equals(dog.getBreed())) {
            updates.put("breed", breed);
        }

        // Check if the age has changed and update it
        if (!age.equals(dog.getAge())) {
            updates.put("age", age);
        }

        // Check if the image has changed and update it
        if (imageChanged) {
            uploadImageAndUpdateDog(updates);
        } else {
            // Update the dog details in Firestore if there are any changes
            updateDogDetails(updates, null);
        }
    }

    private void uploadImageAndUpdateDog(Map<String, Object> updates) {
        // Get the bitmap from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) pictureImageView.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();

        // Convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Define the path and filename in Firebase Storage
        String imageFileName = "dogs/" + dogId + ".jpg";
        StorageReference imageRef = storageRef.child(imageFileName);

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                updates.put("picture", imageUrl);

                // Update the dog details in Firestore
                updateDogDetails(updates, imageUrl);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(EditDogActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }

    private void updateDogDetails(Map<String, Object> updates, String imageUrl) {
        // Update the dog details in Firestore if there are any changes
        if (!updates.isEmpty()) {
            DocumentReference dogRef = db.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("dogs")
                    .document(dog.getId());

            dogRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditDogActivity.this, "Dog updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity and go back to DogAlbumActivity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditDogActivity.this, "Error updating dog", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        } else {
            // No changes were made
            Toast.makeText(EditDogActivity.this, "No changes made", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity and go back to DogAlbumActivity
        }
    }

    private void updateDogDetails(Map<String, Object> updates) {
        updateDogDetails(updates, null);
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
            imageChanged = true;
        }
    }
}
