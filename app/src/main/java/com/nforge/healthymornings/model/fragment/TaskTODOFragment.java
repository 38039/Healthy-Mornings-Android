package com.nforge.healthymornings.model.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.nforge.healthymornings.viewmodel.TaskListViewmodel;

import java.util.List;

public class TaskTODOFragment extends Fragment {
    private ActivityTaskTodoBinding binding;
    private TaskListViewmodel viewModel;

    public TaskTODOFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityTaskTodoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskListViewmodel.class);
        viewModel.populateAdapterWithTasks(null);

        viewModel.taskTitles.observe(getViewLifecycleOwner(), taskTitles -> {
            List<Integer> taskIds = viewModel.taskIdentifiers.getValue();
            if (taskIds == null || taskTitles == null || taskIds.size() != taskTitles.size()) {
                Toast.makeText(requireContext(), "Błąd danych z ViewModel", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.TaskTODOList.removeAllViews();

            for (int i = 0; i < taskTitles.size(); i++) {
                int taskId = taskIds.get(i);
                String taskTitle = taskTitles.get(i);

                CheckBox checkBox = new CheckBox(requireContext());
                checkBox.setText(taskTitle);
                checkBox.setTextColor(getResources().getColor(android.R.color.black));

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        buttonView.setEnabled(false);
                    }
                });

                binding.TaskTODOList.addView(checkBox);
            }
        });

        binding.buttonBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
