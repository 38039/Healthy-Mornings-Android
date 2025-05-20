package com.nforge.healthymornings.ui;

// ANDROID
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.model.SessionManager;


public class LoginActivity extends AppCompatActivity {
    TextView emailTextView, passwordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextView      = findViewById(R.id.emailText   );
        passwordTextView   = findViewById(R.id.passwordText);
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

    public void onLoginButtonClick(View view) {

        // Rzutowanie danych emaila z xml'a do zmiennej tekstowej
        String   accountLoginEmail      = emailTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych hasła z xml'a do zmiennej tekstowej
        // TODO: BCRYPT
        String   accountLoginPassword   = passwordTextView
                                        .getText()
                                        .toString()
                                        .trim();

        accountLoginEmail = "38039@student.atar.edu.pl";
        accountLoginPassword = "123";


        // Zabezpieczenie przed brakiem danych do logowania
        if (    accountLoginEmail.isEmpty() ||
                accountLoginPassword.isEmpty()  )
        {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            Log.w("LoginActivity", "loginAction(): BRAKUJĄCE DANE LOGOWANIA");
            return;
        }


        try {
            // TODO: Przerzucić połączenia i zapytania do bazy do SessionManagera, i przekazywać mu tylko argumenty logowania w celu pozbycia się SQL'owego kodu z Activities

            // Nawiązanie połączenia z bazą danych Healthy Mornings
            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();


            // Sprawdzanie czy użytkownik istnieje w bazie
            java.sql.ResultSet userCredentialsFromDatabase = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE email = ? AND password = ?",
                    new String[]{accountLoginEmail, accountLoginPassword}
            );

            // Przekazywanie danych logowania do menadżera sesji
            if ( userCredentialsFromDatabase.next() ) {
                SessionManager userLoginSession = new SessionManager(this);
                if( !userLoginSession.saveUser(userCredentialsFromDatabase.getInt("id_user")) )
                    throw new Exception("NIE UDAŁO SIĘ ZAPISAĆ DANYCH LOGOWANIA DO SESJI");

               goToTaskListActivity(view);

            } else {
               Log.w("LoginActivity", "loginAction(): NIEPOPRAWNE DANE LOGOWANIA");
               Toast.makeText(this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
            }

            if(!databaseConnector.closeConnection())
                throw new Exception("POŁĄCZENIE Z BAZĄ DANYCH NIE ZAKOŃCZOŁO SIĘ POPRAWNIE");

        } catch (Exception loginException) {
            Log.e("LoginActivity", "loginAction(): " + loginException.getMessage());
            Toast.makeText(this, "Wystąpiły problemy z połączeniem", Toast.LENGTH_SHORT).show();}
    }
}