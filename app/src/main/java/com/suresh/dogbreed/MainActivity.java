package com.suresh.dogbreed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String COUNTER_FLAG = "isCountOn";
    private static final String TAG = "MainActivity";
    
    private Button btnIdBreed, btnIdDog, btnSearchDog;
    private Switch swCountdown;
    private boolean isCountOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIdBreed = findViewById(R.id.btnIdBreed);
        btnIdDog = findViewById(R.id.btnIdDog);
        btnSearchDog = findViewById(R.id.btnSearchDog);
        swCountdown = findViewById(R.id.tgOnCount);

        // Set button listners
        btnIdBreed.setOnClickListener(this);
        btnIdDog.setOnClickListener(this);
        btnSearchDog.setOnClickListener(this);
        swCountdown.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnIdBreed:
                intent = new Intent(MainActivity.this, IdentifyBreeds.class);
                break;
            case R.id.btnIdDog:
                intent = new Intent(MainActivity.this, IdentifyDog.class);
                break;
            case R.id.btnSearchDog:
                intent = new Intent(MainActivity.this, SearchBreeds.class);
                break;
        }
        // Pass counter parameter
        intent.putExtra(COUNTER_FLAG, isCountOn);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isCountOn = isChecked;
    }
}
