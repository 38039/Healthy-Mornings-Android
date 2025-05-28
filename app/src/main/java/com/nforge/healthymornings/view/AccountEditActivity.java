package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.nforge.healthymornings.databinding.ActivityUserAccountEditBinding;
import com.nforge.healthymornings.viewmodel.AccountEditViewmodel;


public class AccountEditActivity extends AppCompatActivity {
    private ActivityUserAccountEditBinding binding;
    private AccountEditViewmodel accountEditViewModel;


    public void goToTaskListActivity() {
        Intent intent = new Intent(this.getApplication(), TaskListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserAccountEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accountEditViewModel = new ViewModelProvider(this).get(AccountEditViewmodel.class);

        setupGenderSpinner();
        accountEditViewModel.loadUserData();

        // Listener nasłuchujący czy użytkownik wprowadził zmiany
        accountEditViewModel.getUserEditResult().observe(this, success -> {
            if ( success != null && !success.isEmpty() ) {
                Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
                Log.v("AccountEditViewmodel", success);
                goToTaskListActivity();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas wprowadzania zmian
        accountEditViewModel.getUserEditError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("AccountEditViewmodel", error);
            }
        });

        // Listener nasłuchujący czy użytkownik został załadowany do widoku
        accountEditViewModel.getUserData().observe(this, user -> {
            if (user != null) {  // TODO: Jeśli użytkownik nie zostanie załadowany to aplikacja się zcrashuje
                binding.NameTextView.setText(user.getName());
                binding.SurnameTextView.setText(user.getSurname());
                binding.UsernameTextView.setText(user.getUsername());
                binding.EmailTextView.setText(user.getEmail());
                binding.BioTextView.setText(user.getBio());
                binding.HeightEditText.setText(String.valueOf(user.getHeight()));
                binding.WeightEditText.setText(String.valueOf(user.getWeight()));

                // TODO: Dodać spinnera do registerActivity na gender i usunąć tego hacka
                int genderIndex = 2;
                if (user.getGender() != null)
                     genderIndex = user.getGender().equalsIgnoreCase("Female") ? 1 : 0;
                binding.GenderSpinner.setSelection(genderIndex);

            } else Toast.makeText(this, "Nie udało się załadować danych użytkownika", Toast.LENGTH_LONG).show();
        });
    }


    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.nforge.healthymornings.R.array.gender_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.GenderSpinner.setAdapter(adapter);
    }

    public void changePassword(View view) {

    }

    public void saveChanges(View view) {

        String accountName = binding.NameTextView
                .getText()
                .toString()
                .trim();

        String accountSurname = binding.SurnameTextView
                .getText()
                .toString()
                .trim();

        String accountGender = binding.GenderSpinner
                .getSelectedItem()
                .toString()
                .trim();

        String accountUsername = binding.UsernameTextView
                .getText()
                .toString()
                .trim();

        String accountEmail = binding.EmailTextView
                .getText()
                .toString()
                .trim();

        String accountBio = binding.BioTextView
                .getText()
                .toString()
                .trim();

        String accountHeightStr = binding.HeightEditText
                .getText()
                .toString()
                .trim();

        if (accountHeightStr.isEmpty())
            accountHeightStr = "-1";

        String accountWeightStr = binding.WeightEditText
                .getText()
                .toString()
                .trim();

        if (accountWeightStr.isEmpty())
            accountWeightStr = "-1";

        accountEditViewModel.updateUserAccount(
                accountName,
                accountSurname,
                accountGender,
                accountUsername,
                accountEmail,
                accountBio,
                Double.parseDouble(accountHeightStr),
                Double.parseDouble(accountWeightStr)
        );
    }
}
