package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.nforge.healthymornings.databinding.ActivityTaskAddBinding;
import com.nforge.healthymornings.viewmodel.TaskAddViewmodel;


public class TaskAddActivity extends AppCompatActivity {
    private TaskAddViewmodel taskAddViewmodel;
    private ActivityTaskAddBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        taskAddViewmodel = new ViewModelProvider(this).get(TaskAddViewmodel.class);

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
}
