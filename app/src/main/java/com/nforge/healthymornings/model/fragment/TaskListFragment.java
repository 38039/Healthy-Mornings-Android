package com.nforge.healthymornings.model.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import com.nforge.healthymornings.databinding.ActivityTaskListBinding;
import com.nforge.healthymornings.view.TaskEditActivity;
import com.nforge.healthymornings.viewmodel.TaskListViewmodel;


public class TaskListFragment extends Fragment {
    private TaskListViewmodel viewModel;
    private ActivityTaskListBinding binding;
    private ArrayAdapter<String> adapter;


    public TaskListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityTaskListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskListViewmodel.class);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
                Objects.requireNonNull(viewModel.taskTitles.getValue()));

        viewModel.populateAdapterWithTasks(adapter);
        binding.TasksList.setAdapter(adapter);

        // Listener nasłuchujący ListView, przekazujący dane klikniętego zadania do TaskEditActivity
        binding.TasksList.setOnItemClickListener((parent, itemView, index, id) -> {
            Integer selectedTaskID = Objects.requireNonNull(viewModel.taskIdentifiers.getValue()).get(index);
            String selectedTaskTitle = viewModel.taskTitles.getValue().get(index);

            Log.v("TaskListFragment", "setOnItemClickListener(): [ID]: " + selectedTaskID);
            Log.v("TaskListFragment", "setOnItemClickListener(): [Tytuł]: " + selectedTaskTitle);

            Intent intent = new Intent(requireContext(), TaskEditActivity.class);
            intent.putExtra("Task ID", selectedTaskID);
            intent.putExtra("Task Name", selectedTaskTitle);
            startActivity(intent);
        });

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis());
        binding.deadlineTaskText.setText(today);
    }

    // Czyści binding, zapobiega wyciekowi pamięci
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
