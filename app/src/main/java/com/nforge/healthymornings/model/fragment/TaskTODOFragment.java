package com.nforge.healthymornings.model.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.databinding.ActivityTaskTodoBinding;
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.repository.UserRepository;
import com.nforge.healthymornings.view.GratulationActivity;
import com.nforge.healthymornings.viewmodel.TaskEditViewmodel;
import com.nforge.healthymornings.viewmodel.TaskListViewmodel;

import java.util.List;

public class TaskTODOFragment extends Fragment {
    private ActivityTaskTodoBinding binding;
    private TaskListViewmodel viewModel;
    private TaskEditViewmodel taskEditViewmodel;
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;

    private int pointsForTask = 0;
    private long points = 0;

    public TaskTODOFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityTaskTodoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskEditViewmodel = new ViewModelProvider(this).get(TaskEditViewmodel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(TaskListViewmodel.class);
        userRepository = new UserRepository(requireContext());

        User currentUser = userRepository.getUserCredentials();
        if (currentUser != null) {
            points = currentUser.getPoints();
            Log.v("StatisticsFragment", "Punkty użytkownika: " + points);
        } else {
            Log.v("StatisticsFragment", "Brak użytkownika lub dane null");
        }

        viewModel.populateAdapterWithTasks(null);
        sharedPreferences = requireContext().getSharedPreferences("task_checkbox_prefs", MODE_PRIVATE);
        viewModel.taskTitles.observe(getViewLifecycleOwner(), taskTitles -> {
            List<Integer> taskIds = viewModel.taskIdentifiers.getValue();

            if (taskIds == null || taskTitles == null || taskIds.size() != taskTitles.size()) {
                Toast.makeText(requireContext(), "Błąd danych z ViewModel", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.TaskTODOList.removeAllViews();

            for (int i = 0; i < taskTitles.size(); i++) {
                final int index = i;
                int taskId = taskIds.get(index);
                String taskTitle = taskTitles.get(index);

                CheckBox checkBox = new CheckBox(requireContext());
                checkBox.setText(taskTitle);
                checkBox.setTextColor(getResources().getColor(android.R.color.black));

                long lastCheckedTime = sharedPreferences.getLong(String.valueOf(taskId), 0);
                long now = System.currentTimeMillis();
                if (lastCheckedTime != 0 && now - lastCheckedTime < 1) {
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
                }

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        sharedPreferences.edit().putLong(String.valueOf(taskId), System.currentTimeMillis()).apply();
                        buttonView.setEnabled(false);

                        taskEditViewmodel.loadTaskData(taskId);

                        taskEditViewmodel.getTaskData().observe(getViewLifecycleOwner(), task -> {
                            if (task != null) {
                                pointsForTask = task.getReward();
                                userRepository.addPointsToUser(pointsForTask);
                                Toast.makeText(requireContext(),"Zdobyto " + pointsForTask + " punktów!", Toast.LENGTH_SHORT).show();

                                taskEditViewmodel.getTaskData().removeObservers(getViewLifecycleOwner());
                            }
                        });

                        Intent intent = new Intent(requireContext(), GratulationActivity.class);
                        startActivity(intent);
                    }
                });

                binding.TaskTODOList.addView(checkBox);
            }
        });

        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Metoda opcjonalna, jeśli chcesz gdzieś udostępnić punkty
    public int getPointsForTask() {
        return pointsForTask;
    }
}

