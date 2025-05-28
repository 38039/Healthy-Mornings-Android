package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.viewmodel.TaskEditViewmodel;


public class TaskEditActivity extends AppCompatActivity {
    private TaskEditViewmodel taskEditViewmodel;
    private EditText inputName;
    private EditText inputCategory;
    private EditText inputDescription;
    private EditText inputPoints;
    private int editedTaskID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        taskEditViewmodel = new ViewModelProvider(this).get(TaskEditViewmodel.class);

        inputName           = findViewById(R.id.TaskNameTextView);
        inputCategory       = findViewById(R.id.TaskCategoryTextView);
        inputDescription    = findViewById(R.id.TaskDescriptionTextView);
        inputPoints         = findViewById(R.id.TaskPointsTextNumber);

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
                Log.v("TaskEditViewmodel", "(): " + success);
                goToTaskListActivity();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas edycji zadania
        taskEditViewmodel.getTaskEditErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("TaskEditViewmodel", "(): " + error);
            }
        });

        // Wczytywanie danych edytowanego zadania do lokalnego rekordu
        taskEditViewmodel.loadTaskData(editedTaskID);

        // Listener nasłuchujący czy zadanie zostało załadowane do widoku
        taskEditViewmodel.getTaskData().observe(this, task -> {
            if (task != null) {
                inputName.setText(task.getName());
                inputCategory.setText(task.getCategory());
                inputDescription.setText(task.getDescription());
                inputPoints.setText(String.valueOf(task.getReward()));
            }
        });
    }

    public void goToTaskListActivity() {
        Intent intent = new Intent(this.getApplication(), TaskListActivity.class);
        startActivity(intent);
        finish();
    }

    public void onDeleteTaskButtonClick(View view) {
        taskEditViewmodel.deleteUserTask(editedTaskID);
    }

    public void onSaveTaskButtonClick(View view) {
        String editedTaskName = inputName
                .getText()
                .toString()
                .trim();

        String editedTaskCategory = inputCategory
                .getText()
                .toString()
                .trim();

        String editedTaskDescription = inputDescription
                .getText()
                .toString()
                .trim();

        String editedTaskPointsRewardStr = inputPoints
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
