package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.viewmodel.TaskAddViewmodel;


public class TaskAddActivity extends AppCompatActivity {
    TaskAddViewmodel taskAddViewmodel;
    TextView taskNameView, categoryTextView, descriptionTextView;
    EditText pointsRewardView;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        taskAddViewmodel = new ViewModelProvider(this).get(TaskAddViewmodel.class);

        taskNameView        = findViewById(R.id.TaskNameView);
        categoryTextView    = findViewById(R.id.CategoryTextView);
        descriptionTextView = findViewById(R.id.DesciptionTextView);
        pointsRewardView    = findViewById(R.id.PointsRewardView);
        addButton           = findViewById(R.id.AddTaskButton);


        // Listener nasłuchujący czy użytkownik się zalogował
        taskAddViewmodel.getTaskAddResultLiveData().observe(this, success -> {
            if ( success != null && !success.isEmpty() ) {
                Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
                Log.v("TaskAddViewmodel", "addTask(): " + success);
                goToTaskListActivity();
            }
        });

        // Listener nasłuchujący czy wystąpił błąd podczas logowania
        taskAddViewmodel.getTaskAddErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Log.w("TaskAddViewmodel", "addTask(): " + error);
            }
        });
    }

    public void goToTaskListActivity() {
        Intent intent = new Intent(this.getApplication(), TaskListActivity.class);
        startActivity(intent);
        finish();
    }

    public void onAddTaskButtonClick(View view) {
        String newTaskName = taskNameView
                .getText()
                .toString()
                .trim();

        String newTaskCategory = categoryTextView
                .getText()
                .toString()
                .trim();

        String newTaskDescription = descriptionTextView
                .getText()
                .toString()
                .trim();

        String newTaskPointsRewardStr = pointsRewardView
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
}
