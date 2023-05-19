package com.example.newtry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DogDetailsActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtBreed;
    private TextView txtAge;
    private ImageView imageView;

    Button btnBack;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_dog_details);

        btnBack = findViewById(R.id.btnBack);
        txtName = findViewById(R.id.txtName);
        txtBreed = findViewById(R.id.txtBreed);
        txtAge = findViewById(R.id.txtAge);
        imageView = findViewById(R.id.imageView);

        // Retrieve the dog object from the intent
        Dog dog = getIntent().getParcelableExtra("dog");

        // Set the dog's details in the activity
        txtName.setText(dog.getName());
        txtBreed.setText("Breed: " + dog.getBreed());
        txtAge.setText("Age: "+ String.valueOf(dog.getAge()));

        // Load and display the dog's image using Picasso library
        Picasso.get().load(dog.getPicture()).into(imageView);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
