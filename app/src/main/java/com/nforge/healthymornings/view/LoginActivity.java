package com.nforge.healthymornings.view;

// ANDROID
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.viewmodel.UserViewModel;


public class LoginActivity extends AppCompatActivity {
    TextView emailTextView, passwordTextView;
    UserViewModel userLoginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextView      = findViewById(R.id.emailText   );
        passwordTextView   = findViewById(R.id.passwordText);

        userLoginViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userLoginViewModel.getLoginResultLiveData().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show();
                Log.v("UserViewModel", "getLoginResultLiveData(): UŻYTKOWNIK ZOSTAŁ POMYŚLNIE ZALOGOWANY");
                goToTaskListActivity();
            }
        });

        userLoginViewModel.getLoginErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("UserViewModel", "getLoginErrorLiveData(): " + error);
            }
        });

    }


    public void goToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    public void goToTaskListActivity() {
        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
        startActivity(intent);
        finish();
    }


    public void onLoginButtonClick(View view) {

        // Rzutowanie danych emaila z xml'a do zmiennej tekstowej
        String   accountLoginEmail      = emailTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych hasła z xml'a do zmiennej tekstowej
        String   accountLoginPassword   = passwordTextView
                                        .getText()
                                        .toString()
                                        .trim();

        accountLoginEmail = "38039@student.atar.edu.pl";
        accountLoginPassword = "123";


        // Zabezpieczenie przed brakiem danych do logowania
        if ( accountLoginEmail.isEmpty() || accountLoginPassword.isEmpty() ) {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            Log.w("LoginActivity", "loginAction(): BRAKUJĄCE DANE LOGOWANIA");
            return;
        }

        userLoginViewModel.loginUser(accountLoginEmail, accountLoginPassword);
    }
}