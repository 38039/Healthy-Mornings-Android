package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
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

public class StatisticsFragment extends Fragment {
    private ActivityStatisticsBinding binding;
    private TaskAllViewmodel viewModel;

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
