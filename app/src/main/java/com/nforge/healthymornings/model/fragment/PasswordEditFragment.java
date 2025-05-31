package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.databinding.ActivityPasswordEditBinding;
import com.nforge.healthymornings.viewmodel.PasswordEditViewmodel;


public class PasswordEditFragment extends Fragment {
    private ActivityPasswordEditBinding binding;
    private PasswordEditViewmodel viewModel;


    public PasswordEditFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityPasswordEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PasswordEditViewmodel.class);
        viewModel.loadUserData();

        // Listener nasłuchujący statusu zmiany hasła
        viewModel.getChangePasswordResult().observe(getViewLifecycleOwner(), result -> {
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show();

            if (result.contains("Hasło zmienione pomyślnie")) {
                Log.v("PasswordEditViewmodel", "changePassword(): " + result);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new TaskListFragment())
                        .commit();
            } else Log.w("PasswordEditViewmodel", "changePassword(): " + result);
        });

        // Listener nasłuchujący czy użytkownik został załadowany do widoku
        viewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            // Zbędna instukcja warunkowa / Użytkownik musi być załadowany by dostać się do tego fragmentu
            binding.passwordEditText.setText(user.getPassword());
            binding.passwordConfirmEditText.setText(user.getPassword());
        });

//        binding.changePasswordButton.setOnClickListener(v -> onChangePasswordButtonClick());
        binding.changePasswordButton.setOnClickListener(this::onChangePasswordButtonClick);

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

    public void onChangePasswordButtonClick(View view) {
        String password = binding.passwordEditText
                .getText()
                .toString()
                .trim();

        String confirmPassword = binding.passwordConfirmEditText
                .getText()
                .toString()
                .trim();

        viewModel.changePassword(password, confirmPassword);
    }

    // Czyści binding, zapobiega wyciekowi pamięci
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
