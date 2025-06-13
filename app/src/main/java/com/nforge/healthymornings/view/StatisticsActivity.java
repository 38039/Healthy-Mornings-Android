package com.nforge.healthymornings.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.databinding.ActivityStatisticsBinding;
import com.nforge.healthymornings.model.data.Statistics;
import com.nforge.healthymornings.viewmodel.TaskAllViewmodel;

public class StatisticsActivity extends AppCompatActivity {

    private TaskAllViewmodel viewModel;
    private ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TaskAllViewmodel.class);

        viewModel.getStatisticsLiveData().observe(this, statistics -> {
            if (statistics != null) {
                showStatistics(statistics);
            }
        });

        viewModel.getTaskLoadErrorLiveData().observe(this, errorMessage -> {
            clearStatisticsViews();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.loadAllTasksForCurrentUser();
    }

    private void showStatistics(Statistics statistics) {
        binding.TasksActive.setText(statistics.getTasksActive());
        binding.TasksCompleted.setText(statistics.getTasksCompleted());
    }


    private void clearStatisticsViews() {
        binding.TasksActive.setText("");
        binding.TasksCompleted.setText("");
    }
}
