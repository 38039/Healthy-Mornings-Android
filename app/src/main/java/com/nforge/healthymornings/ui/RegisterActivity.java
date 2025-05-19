package com.nforge.healthymornings.ui;

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
// JDBC
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;



public class RegisterActivity extends AppCompatActivity {
    TextView newAccountUsernameTextView, newAccountEmailTextView, newAccountPasswordTextView, newAccountPasswordConfirmationTextView;
    Connection connect;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();

        newAccountUsernameTextView = findViewById(R.id.NewAccountUsernameTextView);
        newAccountEmailTextView = findViewById(R.id.NewAccountEmailTextView);
        newAccountPasswordTextView = findViewById(R.id.NewAccountPasswordTextView);
        newAccountPasswordConfirmationTextView = findViewById(R.id.NewAccountPasswordConfirmationTextView);
        dateButton = findViewById(R.id.DateOfBirthButton);
        dateButton.setText(getTodaysDate());


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
                String date = Integer.toString(year)
                                + "-" + Integer.toString(month+1)
                                + "-" + Integer.toString(day);
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

    public void CreateAccount(View v)
    {
        String newAccountUsername = newAccountUsernameTextView.getText().toString().trim();
        String newAccountEmail = newAccountEmailTextView.getText().toString().trim();
        String newAccountPassword = newAccountPasswordTextView.getText().toString().trim();
        String confirmPassword = newAccountPasswordConfirmationTextView.getText().toString().trim();

        if (newAccountUsername.isEmpty() || newAccountEmail.isEmpty() || newAccountPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (datePickerDialog.getDatePicker().getYear() > 2012) {
            Toast.makeText(this, "Proszę wybrać poprawną datę urodzenia", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newAccountPassword.equals(confirmPassword))
        {
            Toast.makeText(this, "The passwords are not the same", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            DatabaseConnectivityJDBC databaseConnectivityJDBC = new DatabaseConnectivityJDBC();
            connect = databaseConnectivityJDBC.establishDatabaseConnection();

                String query = "INSERT INTO users (username, email, password, date_of_birth, is_admin, level) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connect.prepareStatement(query);
                stmt.setString(1, newAccountUsername);
                stmt.setString(2, newAccountEmail);
                stmt.setString(3, newAccountPassword);

                GregorianCalendar gregorianCalendar = new GregorianCalendar(
                        datePickerDialog.getDatePicker().getYear(),
                        datePickerDialog.getDatePicker().getMonth(),
                        datePickerDialog.getDatePicker().getDayOfMonth()
                );
                java.util.Date utilDate = gregorianCalendar.getTime();
                Date selectedDate = new java.sql.Date(utilDate.getTime());

                stmt.setDate(4, selectedDate);


                stmt.setBoolean(5, false);
                stmt.setInt(6, 1);

                int result = stmt.executeUpdate();

                if(result <= 0) {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Registration completed successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
        }
        catch (Exception e)
        {
            Log.e("Register", "Registration error: " + e.getMessage());
            Toast.makeText(this, "Registration error", Toast.LENGTH_SHORT).show();
        }
    }


}
