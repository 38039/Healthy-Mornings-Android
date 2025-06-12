package com.nforge.healthymornings.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.nforge.healthymornings.databinding.ActivityLoginBinding;
import com.nforge.healthymornings.viewmodel.LoginViewmodel;


public class LoginActivity extends AppCompatActivity {
    private LoginViewmodel userLoginViewmodel;
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userLoginViewmodel = new ViewModelProvider(this).get(LoginViewmodel.class);

        // Listener nasłuchujący czy użytkownik się zalogował
        userLoginViewmodel.getLoginResultLiveData().observe(this, success -> {
            if ( success != null && !success.isEmpty() ) {
                Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show();
                Log.v("LoginViewModel", "loginUser(): Użytkownik został pomyślnie zalogowany");
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).putString("userEmail", success).apply();
                goToMainActivity();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas logowania
        userLoginViewmodel.getLoginErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("LoginViewModel", "loginUser(): " + error);
            }
        });
    }


    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    // Obsługa przycisku logowania
    public void onLoginButtonClick(View view) {
        // Rzutowanie danych emaila z xml'a do zmiennej tekstowej
        String   accountLoginEmail      = binding.emailText
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych hasła z xml'a do zmiennej tekstowej
        String   accountLoginPassword   = binding.passwordText
                                        .getText()
                                        .toString()
                                        .trim();

        // Przekazanie danych do view modelu
        userLoginViewmodel.loginUser(accountLoginEmail, accountLoginPassword);
    }
}