package com.suresh.dogbreed;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class IdentifyDog extends AppCompatActivity implements View.OnClickListener {

    private static final int DELAY = 10000;

    private ImageView ivDogBreed1, ivDogBreed2, ivDogBreed3;
    private Button btnNext;
    boolean isCountEnabled;

    private boolean hasAnswered = false;

    private int[] randBreeds = new int[3];
    private int correctBreed;
    private int randDog;
    private TextView tvBreed, tvAnsState, tvCountdown;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_dog);

        tvBreed = findViewById(R.id.tvDogBreed);
        tvAnsState = findViewById(R.id.tvAnswerState);
        tvCountdown = findViewById(R.id.tvCountdown);
        ivDogBreed1 = findViewById(R.id.ivBreed1);
        ivDogBreed2 = findViewById(R.id.ivBreed2);
        ivDogBreed3 = findViewById(R.id.ivBreed3);
        btnNext = findViewById(R.id.btnNext);

        ivDogBreed1.setOnClickListener(this);
        ivDogBreed2.setOnClickListener(this);
        ivDogBreed3.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        // Display / Hide countdown
        isCountEnabled = getIntent().getBooleanExtra(MainActivity.COUNTER_FLAG, false);
        if (!isCountEnabled) {
            tvCountdown.setVisibility(View.INVISIBLE);
        }

        // Load a new dog instance
        loadDogs();
    }

    private void loadDogs() {
        hasAnswered = false;
        tvAnsState.setVisibility(View.INVISIBLE);

        // Generate unique dog breed (positions)
        randBreeds = generateRand(3, Constants.DOG_IMAGES.length);
        correctBreed = randBreeds[generateRand(1, 3)[0]];

        tvBreed.setText(Constants.DOG_BREED_NAMES[correctBreed]);

        // Load random dog for first placeholder
        randDog = generateRand(1, Constants.DOG_IMAGES[randBreeds[0]].length)[0];
        ivDogBreed1.setImageDrawable(getResources().getDrawable(Constants.DOG_IMAGES[randBreeds[0]][randDog]));

        // Load random dog for second placeholder
        randDog = generateRand(1, Constants.DOG_IMAGES[randBreeds[1]].length)[0];
        ivDogBreed2.setImageDrawable(getResources().getDrawable(Constants.DOG_IMAGES[randBreeds[1]][randDog]));

        // Load random dog for third placeholder
        randDog = generateRand(1, Constants.DOG_IMAGES[randBreeds[2]].length)[0];
        ivDogBreed3.setImageDrawable(getResources().getDrawable(Constants.DOG_IMAGES[randBreeds[2]][randDog]));

        // Start countdown
        startCountdown();
    }

    // Validate user answer
    private void validateAnswer(int pos, int correctBreed) {
        if (!hasAnswered) {
            if (pos == correctBreed) {
                tvAnsState.setText(getString(R.string.correct));
                tvAnsState.setTextColor(getResources().getColor(R.color.colorGreen));
                tvAnsState.setVisibility(View.VISIBLE);
            } else {
                tvAnsState.setText(getString(R.string.wrong));
                tvAnsState.setTextColor(getResources().getColor(R.color.colorRed));
                tvAnsState.setVisibility(View.VISIBLE);
            }
            hasAnswered = true;
            stopCountdown();
        } else {
            Toast.makeText(this, getString(R.string.already_choice), Toast.LENGTH_LONG).show();
        }
    }

    // Generate 3 unique random numbers
    private int[] generateRand(int length, int maxSize) {
        int[] rand = new int[length];
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i = 0; i < length; i++) {
            rand[i] = list.get(i);
        }
        return rand;
    }

    // Start the countdown timer
    private void startCountdown() {
        if (isCountEnabled) {
            timer = new CountDownTimer(DELAY, 1000) {
                public void onTick(long millisUntilFinished) {
                    long rmSeconds = millisUntilFinished / 1000;
                    tvCountdown.setText(String.format(getString(R.string.seconds_remaining), rmSeconds));
                }

                public void onFinish() {
                    loadDogs();
                }
            }.start();
        }
    }

    // Stop the countdown timer
    private void stopCountdown() {
        if (isCountEnabled) {
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBreed1:
                validateAnswer(randBreeds[0], correctBreed);
                break;
            case R.id.ivBreed2:
                validateAnswer(randBreeds[1], correctBreed);
                break;
            case R.id.ivBreed3:
                validateAnswer(randBreeds[2], correctBreed);
                break;
            case R.id.btnNext:
                loadDogs();
                break;
        }
    }
}
