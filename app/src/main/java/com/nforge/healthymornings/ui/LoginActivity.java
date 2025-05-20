package com.nforge.healthymornings.ui;

// ANDROID
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// JDBC
import java.sql.*;

// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;



public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToTaskListActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
        startActivity(intent);
        finish();
    }

    public void LoginAction(View view) {
        TextView emailTextView      = findViewById(R.id.emailText   );
        TextView passwordTextView   = findViewById(R.id.passwordText);

        // Rzutowanie danych emaila z xml'a do zmiennej tekstowej
        String   userEmail          = emailTextView
                                    .getText()
                                    .toString()
                                    .trim();

        // Rzutowanie danych hasła z xml'a do zmiennej tekstowej
        // TODO: BCRYPT
        String   userPassword       = passwordTextView
                                    .getText()
                                    .toString()
                                    .trim();

        userEmail = "38039@student.atar.edu.pl";
        userPassword = "123";


        // Zabezpieczenie przed brakiem danych do logowania
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Proszę podać dane logowania.", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            // Nawiązanie połączenia z bazą danych Healthy Mornings
            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Utworzenie zapytania do bazy danych
            ResultSet selectUserResults = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE email = ? AND password = ?",
                    new String[]{userEmail, userPassword}
            );

            // Odpowiedź z bazy danych
            if (selectUserResults.next()) {
               int userId = selectUserResults.getInt("id_user");
               Toast.makeText(this, "Hi " + userId, Toast.LENGTH_SHORT).show();
               String name = selectUserResults.getString("name");

               SharedPreferences sharedPreferences = getSharedPreferences("Healthy Mornings Shared Preferences", MODE_PRIVATE);
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
            } else {
               Log.v("LoginActivity", "Niepoprawne dane logowania");
               Toast.makeText(this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
            }

            if(!databaseConnector.closeConnection())
                throw new Exception("Połączenie z bazą danych nie zostało poprawnie zamknięte");

        } catch (Exception loginException) {
            Log.e("LoginActivity", "loginAction(): " + loginException.getMessage());
            Toast.makeText(this, "Brak dostępu do internetu", Toast.LENGTH_SHORT).show();}
    }
}