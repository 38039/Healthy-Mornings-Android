package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.viewmodel.TaskListViewmodel;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class TaskListActivity extends AppCompatActivity {
    private TaskListViewmodel viewModel;
    TextView deadlineTask;
    ListView tasksListView;
    ArrayAdapter<String> adapter;
    Button addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        viewModel = new ViewModelProvider(this).get(TaskListViewmodel.class);

        deadlineTask  = findViewById(R.id.deadlineTaskText);
        addTaskButton = findViewById(R.id.GoToAddTaskActivityButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                Objects.requireNonNull(viewModel.taskTitles.getValue()));
        viewModel.populateAdapterWithTasks(adapter);

        tasksListView = findViewById(R.id.TasksList);
        tasksListView.setAdapter(adapter);


        // Listener na dodawanie nowego zadania
        addTaskButton.setOnClickListener(v -> {
            startActivity(new Intent(this, TaskAddActivity.class));
            finish();
        });

        // Listener na wybór zadania
        tasksListView.setOnItemClickListener((parent, view, index, id) -> {
            Integer selectedTaskID    = Objects.requireNonNull( viewModel.taskIdentifiers.getValue() ).get(index);
            String  selectedTaskTitle = viewModel.taskTitles.getValue().get(index);

            Log.v("TaskListActivity", "setOnItemClickListener(): [Indeks wybranego zadania]: " + selectedTaskID);
            Log.v("TaskListActivity", "setOnItemClickListener(): [Nazwa wybranego zadania]: "  + selectedTaskTitle);

            Intent intent = new Intent(this, TaskEditActivity.class);
            intent.putExtra("Task ID", selectedTaskID);
            intent.putExtra("Task Name", selectedTaskTitle);
            startActivity(intent);
            finish();
        });



//        deadlineTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                long millis = System.currentTimeMillis();
//                Date date = new Date(millis);
//                SimpleDateFormat hourFormat = new SimpleDateFormat("H", Locale.getDefault());
//                int hour = Integer.parseInt(hourFormat.format(date));
//
//                //hour = 5;
//                if (hour >= 4 && hour <= 9)
//                {
//                    Intent intent = new Intent(TaskListActivity.this, TaskTODOActivity.class);
//                    intent.putStringArrayListExtra("task_list", taskNames);
//                    intent.putExtra("id_user", userId);
//                    startActivity(intent);
//                }
//            }
//        });
        // Wyświetlanie dzisiejszej daty
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis());
        deadlineTask.setText(today);

    }
}
