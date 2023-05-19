package com.example.newtry;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class dogmatcherr extends AppCompatActivity {
    TextView tvQuestion;
    RadioGroup rgChoices;
    ImageButton btnNext;

    private String[][] answerChoices = {
            {"YES, I HAVE KIDS UNDER 12 YEARS OLD", "YES, I HAVE KIDS OVER 12 YEARS OLD", "NO, I DON'T HAVE KIDS AT WALA AKONG BALAK"},
            {"HIGHLY PROTECTIVE, YUNG KAYA AKONG IPAGLABAN", "PROTECTIVE PERO SAKTONG FRIENDLY DIN WITH STRANGERS", "LOVES EVERYONE AT MAHAL NA MAHAL AKO"},
            {"I PREFER A DOG NA DI MASYADONG NATAHOL", "OKAY NA SAKIN YUNG KONTING BARK BARK", "BARKING IS NOT AN ISSUE FOR ME, KAHIT MAMAOS SYA KAKATAHOL"},
            {"BASIC OBEDIENCE", "ADVANCED OBEDIENCE", "NOT AN ISSUE FOR ME"},
            {"I OR SOMEONE ELSE WILL BE HOME MOST OF THE TIME", "ONLY ABOUT 4 HOURS AT A TIME", "MY DOG SHOULD BE FINE BY HIMSELF FOR AT LEAST 8 HOURS"}
    };
    private String[] questions = {
            "Will they have any kids to play with?",
            "Which best describes your future pet’s personality?",
            "In terms of barking, how much noise can you tolerate?",
            "How much training will your new dog receive?",
            "How much time will your new dog be spending alone?"
    };
    private String[] answers = new String[questions.length];
    private int currentQuestionIndex = 0;
    private StringBuilder userAnswer = new StringBuilder();

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_matcherr);

        tvQuestion = findViewById(R.id.tvQuestion);
        rgChoices = findViewById(R.id.rgChoices);
        btnNext = findViewById(R.id.btnNext);

        userAnswer = new StringBuilder();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = rgChoices.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(checkedRadioButtonId);

                    answers[currentQuestionIndex] = selectedRadioButton.getText().toString();
                    userAnswer.append(answers[currentQuestionIndex]);

                    currentQuestionIndex++;

                    if (currentQuestionIndex >= questions.length) {
                        getAnswers();
                    } else {
                        showNextQuestion();
                    }
                }
            }
        });

        showNextQuestion();


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavgationView);
        bottomNavigationView1.setSelectedItemId(R.id.bottom_matcher);

        bottomNavigationView1.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                return true;
            } else if (item.getItemId() == R.id.bottom_matcher) {
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
                showLogoutConfirmationDialog();
                return true;
            } else {
                return false;
            }
        });


    }

    private void showNextQuestion() {
        rgChoices.clearCheck();
        tvQuestion.setText(questions[currentQuestionIndex]);

        RadioButton radioButton1 = findViewById(R.id.rb1);
        RadioButton radioButton2 = findViewById(R.id.rb2);
        RadioButton radioButton3 = findViewById(R.id.rb3);

        radioButton1.setText(answerChoices[currentQuestionIndex][0]);
        radioButton2.setText(answerChoices[currentQuestionIndex][1]);
        radioButton3.setText(answerChoices[currentQuestionIndex][2]);
    }

    private void getAnswers() {
        //Getting answers
        StringBuilder userAnswer = new StringBuilder();

        String kidsAnswer = answers[0];
        String personaAnswer = answers[1];
        String barkAnswer = answers[2];
        String trainingAnswer = answers[3];
        String timeAnswer = answers[4];

        if (kidsAnswer.equals(answerChoices[0][0])) {
            kidsAnswer = "YES UNDER 12";
        } else if (kidsAnswer.equals(answerChoices[0][1])) {
            kidsAnswer = "YES ABOVE 12";
        } else if (kidsAnswer.equals(answerChoices[0][2])) {
            kidsAnswer = "NO";
        }
        userAnswer.append(kidsAnswer).append(" ");

        if (personaAnswer.equals(answerChoices[1][0])) {
            personaAnswer = "HP";
        } else if (personaAnswer.equals(answerChoices[1][1])) {
            personaAnswer = "PF";
        } else if (personaAnswer.equals(answerChoices[1][2])) {
            personaAnswer = "L";
        }
        userAnswer.append(personaAnswer).append(" ");

        if (barkAnswer.equals(answerChoices[2][0])) {
            barkAnswer = "LOW";
        } else if (barkAnswer.equals(answerChoices[2][1])) {
            barkAnswer = "MEDIUM";
        } else if (barkAnswer.equals(answerChoices[2][2])) {
            barkAnswer = "HIGH";
        }
        userAnswer.append(barkAnswer).append(" ");

        if (trainingAnswer.equals(answerChoices[3][0])) {
            trainingAnswer = "BASIC";
        } else if (trainingAnswer.equals(answerChoices[3][1])) {
            trainingAnswer = "ADVANCE";
        } else if (trainingAnswer.equals(answerChoices[3][2])) {
            trainingAnswer = "BASIC";
        }
        userAnswer.append(trainingAnswer).append(" ");

        if (timeAnswer.equals(answerChoices[4][0])) {
            timeAnswer = "HOME";
        } else if (timeAnswer.equals(answerChoices[4][1])) {
            timeAnswer = "4HOURS";
        } else if (timeAnswer.equals(answerChoices[4][2])) {
            timeAnswer = "8HOURS";
        }
        userAnswer.append(timeAnswer);

        rgChoices.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);

        // Pass the userAnswer to the new activity
        Intent intent = new Intent(dogmatcherr.this, DogMatcher.class);
        intent.putExtra("userAnswer", userAnswer.toString());
        startActivity(intent);
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
