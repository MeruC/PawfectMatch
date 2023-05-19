package com.example.newtry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newtry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class regActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etRegEmail, etRegPass;
    Button btnRegister;
    TextView tvLog;

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);

        mAuth = FirebaseAuth.getInstance();
        btnRegister = findViewById(R.id.btnRegister);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPass = findViewById(R.id.etRegPass);
        tvLog = findViewById(R.id.tvLog);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });


        tvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void register() {

        String user = etRegEmail.getText().toString().trim();
        String pass = etRegPass.getText().toString().trim();

        if (user.isEmpty()){

            etRegEmail.setError("Enter Email");

        }
        if (pass.isEmpty()){

            etRegPass.setError("Enter Pass");

        }
        else{

            mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(regActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(regActivity.this, MainActivity.class));
                    }
                    Toast.makeText(regActivity.this, "Registration Failed\n"+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }
    }
}