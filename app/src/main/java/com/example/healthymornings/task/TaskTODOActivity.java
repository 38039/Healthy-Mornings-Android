package com.example.healthymornings.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthymornings.R;
import com.example.healthymornings.data.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskTODOActivity extends AppCompatActivity
{
    Connection connect;
    LinearLayout taskListTODO;
    int userId;
    ArrayList<String> taskListArray = new ArrayList<>();
    ArrayList<Integer> completedTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_todo);

        taskListTODO = findViewById(R.id.TaskTODOList);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        if (userId != -1)
        {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String lastResetDate = sharedPreferences.getString("lastResetDate", "");

            if (!today.equals(lastResetDate))
            {
                resetTaskCompletion();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lastResetDate", today);
                editor.apply();
            }
            loadTODOTasks(userId);
        }
        else
        {
            Toast.makeText(this, "User data could not be found", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetTaskCompletion()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try
        {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connect = databaseConnection.connectionclass();

            if (connect != null)
            {
                String query =
                        "SELECT t.id_task FROM USER_TASKS ut " +
                                "JOIN TASKS t ON ut.id_task = t.id_task " +
                                "WHERE ut.id_user = " + userId;

                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                {
                    int taskId = rs.getInt("id_task");
                    editor.remove("task_" + taskId);
                }
                editor.apply();
            }
        }
        catch (Exception e)
        {
            Log.e("ResetError", "Task reset error: " + e.getMessage());
        }
    }

    private void loadTODOTasks(int userId)
    {
        try
        {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connect = databaseConnection.connectionclass();

            if (connect != null)
            {
                String query =
                        "SELECT t.id_task, t.name FROM USER_TASKS ut " +
                                "JOIN TASKS t ON ut.id_task = t.id_task " +
                                "WHERE ut.id_user = " + userId;

                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                taskListArray.clear();
                taskListTODO.removeAllViews();
                completedTasks.clear();

                int checkBoxId = 1000;

                while (rs.next())
                {
                    int taskId = rs.getInt("id_task");
                    String taskName = rs.getString("name");
                    taskListArray.add(taskName);

                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(taskName);
                    checkBox.setTextColor(getResources().getColor(android.R.color.black));
                    checkBox.setId(checkBoxId++);

                    if (isTaskCompleted(taskId))
                    {
                        checkBox.setChecked(true);
                        checkBox.setEnabled(false);
                    }

                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked)
                        {
                            buttonView.setEnabled(false);
                            completedTasks.add(taskId);
                            updateCompletedTasks(userId, completedTasks.size());

                            Intent intent = new Intent(TaskTODOActivity.this, GratulationActivity.class);
                            startActivity(intent);
                        }
                    });

                    taskListTODO.addView(checkBox);
                }
            }
            else
            {
                Toast.makeText(this, "No connection to the database", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("SQL Error", "Query error: " + e.getMessage());
            Toast.makeText(this, "Error downloading tasks", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isTaskCompleted(int taskId)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("task_" + taskId, false);
    }

    private void updateCompletedTasks(int userId, int completedTaskCount)
    {
        try
        {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connect = databaseConnection.connectionclass();

            if (connect != null)
            {
                Statement st = connect.createStatement();
                String updateQuery = "UPDATE USERS SET points = points + " + completedTaskCount + " WHERE id_user = " + userId;
                st.executeUpdate(updateQuery);

                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                for (Integer taskId : completedTasks)
                {
                    editor.putBoolean("task_" + taskId, true);
                }
                editor.apply();
            }
        }
        catch (Exception e)
        {
            Log.e("SQL Error", "Points update error: " + e.getMessage());
            Toast.makeText(this, "Points update error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (userId != -1)
        {
            loadTODOTasks(userId);
        }
    }

    public void back(View view)
    {
        Intent intent = new Intent(TaskTODOActivity.this, TaskListActivity.class);
        startActivity(intent);
        finish();
    }
}
