package com.example.newtry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText etEmail, etPass;
    Button btnLogin, btnGo;
    TextView tvNotreg;
    private String userId;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvNotreg = findViewById(R.id.tvNotreg);
       //btnGo = findViewById(R.id.btnGo);


        //hide contents
        LinearLayout contents = findViewById(R.id.contents);
        contents.setVisibility(View.GONE);


        //Show contents
        FrameLayout animationContainer = findViewById(R.id.animationContainer);
        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.setAnimation("paww.json");
        animationView.playAnimation();

        // Stop the animation after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);

                contents.setVisibility(View.VISIBLE);

            }
        }, 3000);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });

        /*btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Home.class));
            }
        });*/

        tvNotreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, regActivity.class));

            }
        });
    }

    private void login() {

        String user = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if(user.isEmpty()){

            etEmail.setError("You must enter Email Address");
            return;
        }

        if(pass.isEmpty()){

            etEmail.setError("You must enter Password");
            return;
        }
        else{
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            userId = user.getUid();
                            openHomeActivity();
                        }
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }

                }
            });

        }
    }


    private void openHomeActivity() {
        Intent intent = new Intent(MainActivity.this, Home.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }


}