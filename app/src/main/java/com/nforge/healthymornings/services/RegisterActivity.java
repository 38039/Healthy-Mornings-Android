package com.nforge.healthymornings.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nforge.healthymornings.ui.MainActivity;
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterActivity extends AppCompatActivity
{
    TextView emailInput, passwordInput, confirmPasswordInput, nameInput, x;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.createNameText);
        emailInput = findViewById(R.id.createEmailText);
        passwordInput = findViewById(R.id.createPasswordText);
        confirmPasswordInput = findViewById(R.id.confirmPasswordText);
    }

    public void CreateAccount(View v)
    {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
        {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "The passwords are not the same", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            DatabaseConnectivityJDBC databaseConnectivityJDBC = new DatabaseConnectivityJDBC();
            connect = databaseConnectivityJDBC.establishDatabaseConnection();

            if (connect != null)
            {
                String query = "INSERT INTO USERS (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement stmt = connect.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);

                int result = stmt.executeUpdate();
                if (result > 0)
                {
                    Toast.makeText(this, "Registration completed successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "No connection to database", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("Register", "Registration error: " + e.getMessage());
            Toast.makeText(this, "Registration error", Toast.LENGTH_SHORT).show();
        }
    }
}
