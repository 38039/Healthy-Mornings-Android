package com.nforge.healthymornings.view;

// ANDROID
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
// JAVA
import java.util.Calendar;
import java.util.GregorianCalendar;
// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.viewmodel.RegisterViewmodel;


public class RegisterActivity extends AppCompatActivity {
    TextView newAccountUsernameTextView;
    TextView newAccountEmailTextView;
    TextView newAccountPasswordTextView;
    TextView newAccountPasswordConfirmationTextView;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private RegisterViewmodel userRegisterViewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();

        userRegisterViewmodel = new ViewModelProvider(this).get(RegisterViewmodel.class);

        newAccountUsernameTextView              = findViewById(R.id.NewAccountUsernameTextView);
        newAccountEmailTextView                 = findViewById(R.id.NewAccountEmailTextView);
        newAccountPasswordTextView              = findViewById(R.id.NewAccountPasswordTextView);
        newAccountPasswordConfirmationTextView  = findViewById(R.id.NewAccountPasswordConfirmationTextView);
        dateButton                              = findViewById(R.id.DateOfBirthButton);
        dateButton.setText( getTodaysDate() );


        // Listener nasłuchujący czy użytkownik się zarejestrował
        userRegisterViewmodel.getRegisterResultLiveData().observe(this, success -> {
            if (success != null && !success.isEmpty()) {
                Toast.makeText(this, "Rejestracja przebiegła pomyślnie!", Toast.LENGTH_SHORT).show();
                Log.v("RegisterViewModel", "registerUser(): Użytkownik został pomyślnie zarejestrowany");
                goToLoginActivity(this.getCurrentFocus());
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas rejestracji
        userRegisterViewmodel.getRegisterErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("RegisterViewModel", "registerUser(): " + error);
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();

        return      cal.get(Calendar.DAY_OF_MONTH) + "-"
                + ( cal.get(Calendar.MONTH) + 1 )  + "-"
                +   cal.get(Calendar.YEAR);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            String date = day + "-" + (month+1) + "-" + year;
            dateButton.setText(date);
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

        // Wspaniały hack bez którego data przestawia się na rok 3k. Jebać javę
        GregorianCalendar gregorianCalendar = new GregorianCalendar(
                datePickerDialog.getDatePicker().getYear(),
                datePickerDialog.getDatePicker().getMonth(),
                datePickerDialog.getDatePicker().getDayOfMonth()
        );
        java.util.Date utilDate = gregorianCalendar.getTime();
        java.sql.Date selectedDate = new java.sql.Date(
                utilDate.getTime()
        );

        userRegisterViewmodel.registerUser(
                accountRegisterUsername,
                accountRegisterEmail,
                accountRegisterPassword,
                accountConfirmPassword,
                selectedDate,
                datePickerDialog);
    }
}
