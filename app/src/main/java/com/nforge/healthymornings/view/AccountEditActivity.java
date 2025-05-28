package com.nforge.healthymornings.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.nforge.healthymornings.R;

public class AccountEditActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText surnameEditText;
    private Spinner genderSpinner;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText bioEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_edit);
    }
}
