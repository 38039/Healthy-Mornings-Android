// Klasa do activity_login_user
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
import com.nforge.healthymornings.viewmodel.LoginViewmodel;


public class LoginActivity extends AppCompatActivity {
    LoginViewmodel userLoginViewmodel;
    TextView emailTextView, passwordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLoginViewmodel = new ViewModelProvider(this).get(LoginViewmodel.class);

        emailTextView      = findViewById(R.id.emailText   );
        passwordTextView   = findViewById(R.id.passwordText);


        // Listener nasłuchujący czy użytkownik się zalogował
        userLoginViewmodel.getLoginResultLiveData().observe(this, success -> {
            if ( success != null && !success.isEmpty() ) {
                Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show();
                Log.v("LoginViewModel", "loginUser(): Użytkownik został pomyślnie zalogowany");
                goToTaskListActivity();
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
        finish();
    }

    public void goToTaskListActivity() {
        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
        startActivity(intent);
        finish();
    }


    // Obsługa przycisku logowania
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

        // Przekazanie danych do view modelu
        userLoginViewmodel.loginUser(accountLoginEmail, accountLoginPassword);
    }
}