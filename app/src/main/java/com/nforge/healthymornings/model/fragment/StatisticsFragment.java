package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.databinding.ActivityStatisticsBinding;
import com.nforge.healthymornings.viewmodel.TaskAllViewmodel;
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.repository.UserRepository;

public class StatisticsFragment extends Fragment {
    private ActivityStatisticsBinding binding;
    private TaskAllViewmodel viewModel;
    private UserRepository userRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TaskAllViewmodel.class);
        userRepository = new UserRepository(requireContext());

        User currentUser = userRepository.getUserCredentials();
        if (currentUser != null) {
            long points = currentUser.getPoints();
            Log.v("StatisticsFragment", "Punkty użytkownika: " + points);
            binding.PointsText.setText(String.valueOf(points));
        } else {
            Log.v("StatisticsFragment", "Brak użytkownika lub dane null");
            binding.PointsText.setText("Brak danych");
        }


        viewModel.getStatisticsLiveData().observe(getViewLifecycleOwner(), statistics -> {
            if (statistics != null) {
                binding.TasksActive.setText(String.valueOf(statistics.getTasksActive()));
                binding.TasksCompleted.setText(String.valueOf(statistics.getTasksCompleted()));
            }
        });

        viewModel.getTaskLoadErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            binding.TasksActive.setText("");
            binding.TasksCompleted.setText("");

            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.loadAllTasksForCurrentUser();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

