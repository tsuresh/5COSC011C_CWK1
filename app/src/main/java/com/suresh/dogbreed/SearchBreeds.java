package com.suresh.dogbreed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SearchBreeds extends AppCompatActivity implements View.OnClickListener {

    private static final int DELAY = 5000;

    private EditText etBreed;
    private Button btnSubmit, btnStop;
    private ImageView ivDog;

    private Random rand;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_breeds);
        etBreed = findViewById(R.id.etBreed);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnStop = findViewById(R.id.btnStop);
        ivDog = findViewById(R.id.ivDog);

        rand = new Random();

        btnSubmit.setOnClickListener(this);
        btnStop.setOnClickListener(this);

    }

    // Validate user input
    private boolean validateInput() {
        if (etBreed.getText().length() > 0) {
            return true;
        } else {
            Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    // Load new image to the imageview
    private void loadNewImage(int breed) {
        int dogPos = rand.nextInt(Constants.DOG_IMAGES[breed].length);
        ivDog.setImageDrawable(getResources().getDrawable(Constants.DOG_IMAGES[breed][dogPos]));
    }

    // Start the timer and loading images
    private void startLoading(final int breed) {
        btnSubmit.setEnabled(false);
        etBreed.setEnabled(false);
        btnStop.setEnabled(true);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadNewImage(breed);
                    }
                });
            }
        }, 0, DELAY);
    }

    // Stop the countdown
    private void stopLoading() {
        btnSubmit.setEnabled(true);
        etBreed.setEnabled(true);
        btnStop.setEnabled(false);
        timer.cancel();
    }

    // Check if the dog breed is there in the dataset
    private int matchBreed(String category) throws BreedNotFoundException {
        int pos = 0;
        for (String cat : Constants.DOG_BREED_NAMES) {
            if (cat.equalsIgnoreCase(category)) {
                return pos;
            }
            pos++;
        }
        throw new BreedNotFoundException(getString(R.string.breed_not_found));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (validateInput()) {
                    int breed = 0;
                    try {
                        breed = matchBreed(etBreed.getText().toString());
                        startLoading(breed);
                    } catch (BreedNotFoundException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btnStop:
                stopLoading();
                break;
        }
    }
}
