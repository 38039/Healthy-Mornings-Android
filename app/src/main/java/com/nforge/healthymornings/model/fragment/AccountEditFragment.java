package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.databinding.ActivityUserAccountEditBinding;
import com.nforge.healthymornings.viewmodel.AccountEditViewmodel;


public class AccountEditFragment extends Fragment {
    private ActivityUserAccountEditBinding binding;
    private AccountEditViewmodel accountEditViewModel;


    public AccountEditFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityUserAccountEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountEditViewModel = new ViewModelProvider(this).get(AccountEditViewmodel.class);

        setupGenderSpinner();
        accountEditViewModel.loadUserData();

        // Listener nasłuchujący czy użytkownik wprowadził zmiany
        accountEditViewModel.getUserEditResult().observe(getViewLifecycleOwner(), success -> {
            if (success != null && !success.isEmpty()) {
                Toast.makeText(getContext(), success, Toast.LENGTH_SHORT).show();
                Log.v("AccountEditViewmodel", success);

                // Manualna zamiana fragmentów jest wymagana, inaczej każdy fragment zapisywałby się na stosie
                // co doprowadziłoby do sytuacji gdzie po cofnięciu się z fragmentu użytkownik nie powcałby do listy zadań
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new TaskListFragment())
                        .commit();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas wprowadzania zmian
        accountEditViewModel.getUserEditError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                Log.w("AccountEditViewmodel", error);
            }
        });

        // Listener nasłuchujący czy użytkownik został załadowany do widoku
        accountEditViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) { // TODO: Jeśli użytkownik nie zostanie załadowany to aplikacja się zcrashuje
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
            } else Toast.makeText(getContext(), "Nie udało się załadować danych użytkownika", Toast.LENGTH_LONG).show();
        });

        binding.SaveChangesButton.setOnClickListener(this::saveChanges);

        // Listener nasłuchujący czy użytkownik kliknął w przycisk cofnij
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new TaskListFragment())
                        .commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(), callback
        );
    }

    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                com.nforge.healthymornings.R.array.gender_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.GenderSpinner.setAdapter(adapter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
