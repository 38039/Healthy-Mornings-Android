package com.example.healthymornings.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthymornings.data.DatabaseConnection;
import com.example.healthymornings.R;
import com.example.healthymornings.task.TaskListActivity;

public class LoginActivity extends AppCompatActivity
{
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void RegisterAction(View v)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void LoginAction(View v)
    {
        TextView emailField = findViewById(R.id.emailText);
        TextView passwordField = findViewById(R.id.passwordText);

        String inputEmail = emailField.getText().toString().trim();
        String inputPassword = passwordField.getText().toString().trim();

        if (inputEmail.isEmpty() || inputPassword.isEmpty())
        {
            Toast.makeText(this, "Wpisz email i hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connect = databaseConnection.connectionclass();
            if(connect!=null)
            {
                String query = "SELECT * FROM USERS WHERE email = '" + inputEmail + "' AND password = '" + inputPassword + "'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                if (rs.next())
                {
                    int userId = rs.getInt("id_user");
                    //Toast.makeText(this, "Hi " + userId, Toast.LENGTH_SHORT).show();
                    String name = rs.getString("name");
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.clear();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("userId", userId);
                    editor.putString("name", name);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Log.e("Login", "Wrong email or password");
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                ConnectionResult = "Check Connection";
                Log.e("Database Connection", "Connection failed: " + ConnectionResult);
            }
        }
        catch (Exception ex)
        {
            Log.e("SQL Error", "Error executing query: " + ex.getMessage());
        }
    }
}