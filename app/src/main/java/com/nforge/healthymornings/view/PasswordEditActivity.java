package com.nforge.healthymornings.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.nforge.healthymornings.databinding.ActivityPasswordEditBinding;
import com.nforge.healthymornings.viewmodel.PasswordEditViewmodel;


public class PasswordEditActivity extends ComponentActivity {
    private ActivityPasswordEditBinding binding;
    private PasswordEditViewmodel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PasswordEditViewmodel.class);
        viewModel.loadUserData();

        // Listener nasłuchujący statusu zmiany hasła
        viewModel.getChangePasswordResult().observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

                if (result.contains("Hasło zmienione pomyślnie")) {
                    Log.v("PasswordEditViewmodel", "changePassword(): " + result);
                    finish();
                } else Log.w("PasswordEditViewmodel", "changePassword(): " + result);
            }
        });

        // Listener nasłuchujący czy użytkownik został załadowany do widoku
        viewModel.getUserData().observe(this, user -> {
            if (user != null) {  // TODO: Jeśli użytkownik nie zostanie załadowany to aplikacja się zcrashuje
                binding.passwordEditText.setText(user.getPassword());
                binding.passwordConfirmEditText.setText(user.getPassword());
            } else Log.e("PasswordEditActivity", "getUserData(): Nie udało się załadować danych użytkownika");
        });
    }

    public void onChangePasswordButtonClick(View view) {
        String password = binding.passwordEditText
                .getText()
                .toString()
                .trim();

        String confirmPassword = binding.passwordConfirmEditText
                .getText()
                .toString()
                .trim();


        viewModel.changePassword(
                password,
                confirmPassword
        );
    }
}
