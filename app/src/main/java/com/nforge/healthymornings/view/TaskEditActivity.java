package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.nforge.healthymornings.databinding.ActivityTaskEditBinding;
import com.nforge.healthymornings.viewmodel.TaskEditViewmodel;


public class TaskEditActivity extends AppCompatActivity {
    private int editedTaskID;
    private TaskEditViewmodel taskEditViewmodel;
    private ActivityTaskEditBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        taskEditViewmodel = new ViewModelProvider(this).get(TaskEditViewmodel.class);

        // Pobieranie danych edytowanego aktualnie zadania z pamięci krótkotrwałej
        Intent intent = getIntent();
        editedTaskID = intent.getIntExtra("Task ID", -1);
        String taskName = intent.getStringExtra("Task Name");

        Log.v("TaskEditActivity", "onCreate() [Nazwa edytowanego zadania]: " + taskName);
        Log.v("TaskEditActivity", "onCreate() [Identyfikator edytowanego zadania]: " + editedTaskID);

        // Listener nasłuchujący czy zadanie zostało poprawnie edytowane
        taskEditViewmodel.getTaskEditResultLiveData().observe(this, success -> {
            if (success != null && !success.isEmpty()) {
                Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
                Log.v("TaskEditViewmodel", "updateUserTask(): " + success);
                finish();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas edycji zadania
        taskEditViewmodel.getTaskEditErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("TaskEditViewmodel", "updateUserTask(): " + error);
            }
        });

        // Wczytywanie danych edytowanego zadania do lokalnego rekordu
        taskEditViewmodel.loadTaskData(editedTaskID);

        // Listener nasłuchujący czy zadanie zostało załadowane do widoku
        taskEditViewmodel.getTaskData().observe(this, task -> {
            if (task != null) {
                binding.TaskNameTextView.setText(task.getName());
                binding.TaskCategoryTextView.setText(task.getCategory());
                binding.TaskDescriptionTextView.setText(task.getDescription());
                binding.TaskPointsTextNumber.setText(String.valueOf(task.getReward()));
            }
        });
    }

    public void onDeleteTaskButtonClick(View view) {
        taskEditViewmodel.deleteUserTask(editedTaskID);
    }

    public void onSaveTaskButtonClick(View view) {
        String editedTaskName = binding.TaskNameTextView
                .getText()
                .toString()
                .trim();

        String editedTaskCategory = binding.TaskCategoryTextView
                .getText()
                .toString()
                .trim();

        String editedTaskDescription = binding.TaskDescriptionTextView
                .getText()
                .toString()
                .trim();

        String editedTaskPointsRewardStr = binding.TaskPointsTextNumber
                .getText()
                .toString();

        if (editedTaskPointsRewardStr.isEmpty())
            editedTaskPointsRewardStr = "-1";


        taskEditViewmodel.updateUserTask(
                editedTaskID,
                editedTaskName,
                editedTaskCategory,
                editedTaskDescription,
                Integer.parseInt(editedTaskPointsRewardStr)
        );
    }
}
