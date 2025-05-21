package com.nforge.healthymornings.view;

// ANDROID
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// JAVA
import java.util.Calendar;
import java.util.GregorianCalendar;
// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;


public class RegisterActivity extends AppCompatActivity {
    TextView newAccountUsernameTextView;
    TextView newAccountEmailTextView;
    TextView newAccountPasswordTextView;
    TextView newAccountPasswordConfirmationTextView;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();

        newAccountUsernameTextView              = findViewById(R.id.NewAccountUsernameTextView);
        newAccountEmailTextView                 = findViewById(R.id.NewAccountEmailTextView);
        newAccountPasswordTextView              = findViewById(R.id.NewAccountPasswordTextView);
        newAccountPasswordConfirmationTextView  = findViewById(R.id.NewAccountPasswordConfirmationTextView);
        dateButton                              = findViewById(R.id.DateOfBirthButton);


        // TODO: Spróbować pozbyć się metody getTodaysDate() gdyż to musi być zwracane tylko raz
        dateButton.setText(
                getTodaysDate()
        );
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();

        return      cal.get(Calendar.YEAR)        + "-"
                + ( cal.get(Calendar.MONTH) + 1 ) + "-"
                +   cal.get(Calendar.DAY_OF_MONTH) ;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = year + "-" + (month+1) + "-" + day;
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this,
                AlertDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRegisterButtonClick(View v) {

        // Rzutowanie danych nazwy konta z xml'a do zmiennej tekstowej
        String accountRegisterUsername  = newAccountUsernameTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych emaila z xml'a do zmiennej tekstowej
        String accountRegisterEmail     = newAccountEmailTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych hasła z xml'a do zmiennej tekstowej
        String accountRegisterPassword  = newAccountPasswordTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych potwierdzenia hasła z xml'a do zmiennej tekstowej
        String accountConfirmPassword   = newAccountPasswordConfirmationTextView
                                        .getText()
                                        .toString()
                                        .trim();


        // Zabezpieczenie przed brakiem danych do rejestracji
        if (    accountRegisterUsername.isEmpty() ||
                accountRegisterEmail.isEmpty()    ||
                accountRegisterPassword.isEmpty() ||
                accountConfirmPassword.isEmpty()    )
        {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aplikacja odmówi rejestracji użytkownikowi poniżej 12-13 lat
        if (datePickerDialog.getDatePicker().getYear() > 2012) {
            Toast.makeText(this, "Proszę wybrać poprawną datę urodzenia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aplikacja sprawdzi czy hasła są takie same
        if (!accountRegisterPassword.equals(accountConfirmPassword)) {
            Toast.makeText(this, "Proszę podać takie same hasła", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            // TODO: Przerzucić połączenia i zapytania do bazy do SessionManagera, i przekazywać mu tylko argumenty rejestracji w celu pozbycia się SQL'owego kodu z Activities

            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // TODO: Usprawnić ten szajs
            GregorianCalendar gregorianCalendar = new GregorianCalendar(
                    datePickerDialog.getDatePicker().getYear(),
                    datePickerDialog.getDatePicker().getMonth(),
                    datePickerDialog.getDatePicker().getDayOfMonth()
            );
            java.util.Date utilDate = gregorianCalendar.getTime();
            java.sql.Date selectedDate = new java.sql.Date(utilDate.getTime());

            databaseConnector.executeSQLQuery(
                    "INSERT INTO users (username, email, password, date_of_birth, is_admin, level) VALUES (?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            accountRegisterUsername,            // USERNAME
                            accountRegisterEmail,               // EMAIL
                            accountRegisterPassword,            // PASSWORD
                            selectedDate,                       // BIRTH DATE
                            false,                              // IS_ADMIN
                            1                                   // LEVEL
                    }
            );

        } catch (Exception registrationException) {
            Toast.makeText(this, "Użytkownik nie został zarejestrowany", Toast.LENGTH_SHORT).show();
            Log.e("RegisterActivity", "onRegisterButtonClick(): " + registrationException.getMessage());
        }

        Toast.makeText(this, "Rejestracja przebiegła poprawnie", Toast.LENGTH_SHORT).show();
        goToLoginActivity(this.getCurrentFocus());
    }
}
