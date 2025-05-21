package com.nforge.healthymornings.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;

import java.sql.*;

public class TaskAddActivity extends AppCompatActivity {

    Connection connect;

    TextView taskNameView, categoryTextView, descriptionTextView;
    EditText pointsRewardView;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        taskNameView = findViewById(R.id.TaskNameView);
        categoryTextView = findViewById(R.id.CategoryTextView);
        descriptionTextView = findViewById(R.id.DesciptionTextView);
        pointsRewardView = findViewById(R.id.PointsRewardView);
        addButton = findViewById(R.id.AddTaskButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Healthy Mornings Shared Preferences", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("userId", -1);
                Log.v("TaskAddActivity", "userId: " + userId);

                // Otwarcie połączenia z bazą danych
                DatabaseConnectivityJDBC databaseConnectivityJDBC = new DatabaseConnectivityJDBC();
                connect = databaseConnectivityJDBC.establishDatabaseConnection();

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
                        .toString();

                if (newTaskName.isEmpty()) {
                    Toast.makeText(TaskAddActivity.this, "Enter a name for the task", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newTaskCategory.isEmpty()) {
                    Toast.makeText(TaskAddActivity.this, "Enter a category for the task", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newTaskDescription.isEmpty()) {
                    Toast.makeText(TaskAddActivity.this, "Enter a description for the task", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newTaskPointsRewardStr.isEmpty()) {
                    Toast.makeText(TaskAddActivity.this, "Enter a proper point reward for the task", Toast.LENGTH_SHORT).show();
                    return;
                }

                int newTaskPointsReward = Integer.parseInt(newTaskPointsRewardStr);

                if (newTaskPointsReward < 0) {
                    Toast.makeText(TaskAddActivity.this, "Task point reward needs to be positive", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Sprawdzanie, czy zadanie o tej samej nazwie już istnieje
                    String checkQuery = "SELECT id_task FROM TASKS WHERE name = ?";
                    PreparedStatement checkStmt = connect.prepareStatement(checkQuery);
                    checkStmt.setString(1, newTaskName);
                    Log.v("TaskAddActivity", "checkStmt: " + checkStmt.toString());
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        Toast.makeText(TaskAddActivity.this, "Task already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    rs.close();
                    checkStmt.close();


                    // Dodawanie nowego zadania do bazy danych
                    String insertTask = "INSERT INTO TASKS (name, category, description, points_reward) VALUES (?, ?, ?, ?) RETURNING id_task;";
                    PreparedStatement insertStmt = connect.prepareStatement(insertTask);

                    insertStmt.setString(1, newTaskName);
                    insertStmt.setString(2, newTaskCategory);
                    insertStmt.setString(3, newTaskDescription);
                    insertStmt.setInt(4, newTaskPointsReward);

                    ResultSet insertRs = insertStmt.executeQuery();
                    Log.v("TaskAddActivity", "insertRs: " + insertRs.toString());

                    if (!insertRs.next()) {
                        Toast.makeText(TaskAddActivity.this, "Failed to create task: ", Toast.LENGTH_SHORT).show();
                        insertRs.close();
                        insertStmt.close();
                        return;
                    }

                    int taskId = insertRs.getInt("id_task");

                    insertRs.close();
                    insertStmt.close();



                    // Dodawanie zadania do listy zadań użytkownika
                    String insertUserTask = "INSERT INTO user_tasks(id_user, id_task, status) VALUES (?, ?, ?::task_status);";
                    PreparedStatement insertUserTaskStmt = connect.prepareStatement(insertUserTask);
                    insertUserTaskStmt.setInt(1, userId);
                    insertUserTaskStmt.setInt(2, taskId);
                    // W BAZIE DANYCH STATUS ZAPISYWANY JEST JAKO ENUM Z (DONE, PENDING, SKIPPED) A NIE JAKO STRING
                    insertUserTaskStmt.setString(3, "pending");

                    insertUserTaskStmt.executeUpdate();
                    insertUserTaskStmt.close();

                    Log.v("TaskAddActivity", "taskId: ");


                    connect.close();

                } catch (Exception e) {
                    Log.e("TaskAddActivity", "Error adding tasks: " + e.getMessage());
                    Toast.makeText(TaskAddActivity.this, "Error adding tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(TaskAddActivity.this, TaskListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
