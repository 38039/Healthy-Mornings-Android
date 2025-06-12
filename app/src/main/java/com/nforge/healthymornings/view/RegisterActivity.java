package com.nforge.healthymornings.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.nforge.healthymornings.databinding.ActivityRegisterBinding;
import com.nforge.healthymornings.viewmodel.RegisterViewmodel;
import com.nforge.healthymornings.R;


public class RegisterActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private RegisterViewmodel userRegisterViewmodel;
    private ActivityRegisterBinding binding;
    private Spinner genderSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        genderSpinner = binding.genderSpinner;
        String[] genders ={"male", "female", "other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, genders );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        userRegisterViewmodel = new ViewModelProvider(this).get(RegisterViewmodel.class);
        initDatePicker();

        binding.DateOfBirthButton.setText( getTodaysDate() );


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
            binding.DateOfBirthButton.setText(date);
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
        String accountRegisterUsername  = binding.NewAccountUsernameTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych emaila z xml'a do zmiennej tekstowej
        String accountRegisterEmail     = binding.NewAccountEmailTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych hasła z xml'a do zmiennej tekstowej
        String accountRegisterPassword  = binding.NewAccountPasswordTextView
                                        .getText()
                                        .toString()
                                        .trim();

        // Rzutowanie danych potwierdzenia hasła z xml'a do zmiennej tekstowej
        String accountConfirmPassword   = binding.NewAccountPasswordConfirmationTextView
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

        String selectedGender = genderSpinner.getSelectedItem().toString();

        userRegisterViewmodel.registerUser(
                accountRegisterUsername,
                accountRegisterEmail,
                accountRegisterPassword,
                accountConfirmPassword,
                selectedDate,
                selectedGender,
                datePickerDialog);
    }
}
