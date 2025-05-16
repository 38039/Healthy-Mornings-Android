package com.nforge.healthymornings.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Toast;
import android.view.View;
import android.os.Bundle;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.services.LoginActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // KURWA
        //int userId = sharedPreferences.getInt("userId", -1);
        //if (userId == -1) Toast.makeText(this, "User data could not be found" + userId, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void taskList(View view) {
        Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
        startActivity(intent);
        finish();
    }
}