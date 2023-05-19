package com.example.newtry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.slider.Slider;

public class startDogMatcher extends AppCompatActivity {


    @Override
    public void onBackPressed() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_dogmatcher);


        initialize();


    }

    private void initialize() {

        Button btnStart;
        btnStart = findViewById(R.id.btnStart);

        //Hide
        btnStart.setVisibility(View.GONE);

        FrameLayout animationContainer = findViewById(R.id.animationContainer);
        LottieAnimationView animationView = findViewById(R.id.animationFind);
        animationView.setAnimation("find.json");
        animationView.playAnimation();

        // Stop the animation after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                btnStart.setVisibility(View.VISIBLE);


            }
        }, 2000);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the DogMatcher activity
                Intent intent = new Intent(startDogMatcher.this, dogmatcherr.class);
                startActivity(intent);


            }
        });
    }
}