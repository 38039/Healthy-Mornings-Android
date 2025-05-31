package com.nforge.healthymornings.model.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.nforge.healthymornings.databinding.ActivityTaskListBinding;
import com.nforge.healthymornings.model.data.Task;
import com.nforge.healthymornings.model.holder.TaskAdapter;
import com.nforge.healthymornings.view.TaskEditActivity;
import com.nforge.healthymornings.viewmodel.TaskListViewmodel;


public class TaskListFragment extends Fragment {
    private TaskListViewmodel viewModel;
    private ActivityTaskListBinding binding;
    private TaskAdapter adapter; // dodane pole


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

        // Ustaw adapter z pustą listą (tymczasowo)
        adapter = new TaskAdapter(requireContext(), List.of(), selectedTask -> {
            Log.v("TaskListFragment", "Kliknięto zadanie [ID]: " + selectedTask.getID());
            Intent intent = new Intent(requireContext(), TaskEditActivity.class);
            intent.putExtra("Task ID", selectedTask.getID());
            intent.putExtra("Task Name", selectedTask.getName());
            startActivity(intent);
        });

        binding.TasksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.TasksRecyclerView.setAdapter(adapter);

        // Obserwuj dane z ViewModel
        viewModel.getTasks().observe(getViewLifecycleOwner(), updatedTaskList -> {
            adapter.setTasks(updatedTaskList);
            adapter.notifyDataSetChanged();
        });

        // Wczytaj dane
        viewModel.loadTasks(requireContext(), null);

//        viewModel.loadTasks(requireContext(), () -> {
//            List<Task> taskList = viewModel.tasks.getValue();
//
//            TaskAdapter adapter = new TaskAdapter(requireContext(), taskList, selectedTask -> {
//                Log.v("TaskListFragment", "Kliknięto zadanie [ID]: " + selectedTask.getID());
//                Intent intent = new Intent(requireContext(), TaskEditActivity.class);
//                intent.putExtra("Task ID", selectedTask.getID());
//                intent.putExtra("Task Name", selectedTask.getName());
//                startActivity(intent);
//            });
//
//            binding.TasksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//            binding.TasksRecyclerView.setAdapter(adapter);
//        });

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
