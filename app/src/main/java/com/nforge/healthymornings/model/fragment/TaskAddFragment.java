package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.databinding.ActivityTaskAddBinding;
import com.nforge.healthymornings.viewmodel.TaskAddViewmodel;


public class TaskAddFragment extends Fragment {
    private TaskAddViewmodel taskAddViewmodel;
    private ActivityTaskAddBinding binding;


    public TaskAddFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityTaskAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskAddViewmodel = new ViewModelProvider(this).get(TaskAddViewmodel.class);

        // Listener nasłuchujący czy użytkownik dodał zadanie
        taskAddViewmodel.getTaskAddResultLiveData().observe(getViewLifecycleOwner(), success -> {
            if (success != null && !success.isEmpty()) {
                Toast.makeText(requireContext(), success, Toast.LENGTH_SHORT).show();
                Log.v("TaskAddViewmodel", "addTask(): " + success);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas dodawania zadania
        taskAddViewmodel.getTaskAddErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                Log.w("TaskAddViewmodel", "addTask(): " + error);
            }
        });

        // Przycisk dodania zadania
        binding.AddTaskButton.setOnClickListener(v -> onAddTaskButtonClick());
    }

    private void onAddTaskButtonClick() {
        String newTaskName = binding.TaskNameView
                .getText()
                .toString()
                .trim();

        String newTaskCategory = binding.CategoryTextView
                .getText()
                .toString()
                .trim();

        String newTaskDescription = binding.DesciptionTextView
                .getText()
                .toString()
                .trim();

        String newTaskPointsRewardStr = binding.PointsRewardView
                .getText()
                .toString()
                .trim();

        if (newTaskPointsRewardStr.isEmpty())
            newTaskPointsRewardStr = "-1";

        taskAddViewmodel.addTask(
                newTaskName,
                newTaskCategory,
                newTaskDescription,
                Integer.parseInt(newTaskPointsRewardStr)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
