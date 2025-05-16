package com.nforge.healthymornings.services;

// ANDROID
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// JDBC
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// HEALTHY MORNINGS
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.ui.TaskListActivity;
import com.nforge.healthymornings.R;

public class LoginActivity extends AppCompatActivity {
    Connection databaseConnection = null;
    String ConnectionResult = "";


    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void RegisterAction(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void LoginAction(View v) {
        TextView emailTextView      = findViewById(R.id.emailText   );
        TextView passwordTextView   = findViewById(R.id.passwordText);
        TextView nameText           = findViewById(R.id.nameText);

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

        // Zabezpieczenie przed brakiem danych do logowania
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Proszę podać dane logowania.", Toast.LENGTH_SHORT).show();
            return;
        }




        try {
            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();

            // Ustawienie danych połączenia z bazą danych
            databaseConnector.provideDatabaseConnectionDetails(
                    "healthy_mornings",
                    "136.244.87.135",
                    "5432",
                    "milosz",
                    "Dq5PQx%SjxTofg",
                    "application"
            );

            // Utworzenie połączenia z bazą danych
            databaseConnection = databaseConnector.establishDatabaseConnection();
            ConnectionResult = databaseConnection != null ? "Connection successful" : "Connection failed";
            Log.e("Database Connection", ConnectionResult);

            if (databaseConnection != null) {
                String query = "SELECT * FROM users WHERE email = '" + userEmail + "' AND password = '" + userPassword + "'";
                Statement st = databaseConnection.createStatement();
                ResultSet rs = st.executeQuery(query);


                if (rs.next())
                {
                    int userId = rs.getInt("id_user");
                    Toast.makeText(this, "Hi " + userId, Toast.LENGTH_SHORT).show();
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
        catch (Exception ex) { Log.e("LoginActivity", "Polecenie nie wykonano: " + ex.getMessage(), ex); }
    }
}