package com.suresh.dogbreed;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class IdentifyBreeds extends AppCompatActivity implements View.OnClickListener {

    private static final int DELAY = 10000;

    private ImageView ivDogBreed;
    private Spinner spDogBreed;
    private Button btnSubmit;
    private TextView tvAnsState, tvCorrectAns, tvCountdown;

    private int breed;
    private boolean hasAnswered = false;

    private Random rand;

    boolean isCountEnabled;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_breeds);

        // Initialise the UI components
        ivDogBreed = findViewById(R.id.ivBreed);
        spDogBreed = findViewById(R.id.spDogBreed);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvAnsState = findViewById(R.id.tvAnswerState);
        tvCorrectAns = findViewById(R.id.tvCorrectAns);
        tvCountdown = findViewById(R.id.tvCountdown);

        btnSubmit.setOnClickListener(this);

        // Generate random numbers
        rand = new Random();

        // Set dog breed names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Constants.DOG_BREED_NAMES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDogBreed.setAdapter(adapter);

        // Display / Hide countdown
        isCountEnabled = getIntent().getBooleanExtra(MainActivity.COUNTER_FLAG, false);
        if (!isCountEnabled) {
            tvCountdown.setVisibility(View.INVISIBLE);
        }

        // Load new dog
        newDog();
    }

    // Load new dog to imageview
    private void newDog() {
        // Generate random breed
        breed = rand.nextInt(Constants.DOG_BREED_NAMES.length);
        hasAnswered = false;

        // Update views
        btnSubmit.setText(getString(R.string.submit));
        tvAnsState.setVisibility(View.INVISIBLE);
        tvCorrectAns.setVisibility(View.INVISIBLE);

        // Set image and start countdown
        int pos = rand.nextInt(Constants.DOG_IMAGES[breed].length);
        ivDogBreed.setImageDrawable(getResources().getDrawable(Constants.DOG_IMAGES[breed][pos]));

        // Start countdown timer
        startCountdown();
    }

    // Validate if the given input is correct
    private void validateChoice(int correctBreed, int chosenBreed) {
        if (chosenBreed == correctBreed) {
            tvAnsState.setText(getString(R.string.correct));
            tvAnsState.setTextColor(getResources().getColor(R.color.colorGreen));
            tvAnsState.setVisibility(View.VISIBLE);
        } else {
            tvAnsState.setText(getString(R.string.wrong));
            tvAnsState.setTextColor(getResources().getColor(R.color.colorRed));
            tvAnsState.setVisibility(View.VISIBLE);
            tvCorrectAns.setText(Constants.DOG_BREED_NAMES[correctBreed]);
            tvCorrectAns.setVisibility(View.VISIBLE);
        }
        hasAnswered = true;
        btnSubmit.setText(getString(R.string.next));
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
                    validateChoice(breed, spDogBreed.getSelectedItemPosition());
                    tvCountdown.setText(getString(R.string.times_up));
                }
            }.start();
        }
    }

    // Stop countdown
    private void stopCountdown() {
        if (isCountEnabled) {
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (!hasAnswered) {
                    stopCountdown();
                    validateChoice(breed, spDogBreed.getSelectedItemPosition());
                } else {
                    newDog();
                }
                break;
        }
    }
}
