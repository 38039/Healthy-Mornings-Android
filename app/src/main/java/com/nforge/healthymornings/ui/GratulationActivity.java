package com.nforge.healthymornings.ui;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.nforge.healthymornings.R;

public class GratulationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratulation);

        new Handler().postDelayed(() -> {
            finish();
        }, 3000);
    }
}