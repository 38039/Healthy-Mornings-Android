package com.example.healthymornings.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

public class TaskListActivity extends AppCompatActivity
{
    Connection connect;
    TextView taskList, deadlineTask;
    int userId;
    ArrayList<String> taskListArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        taskList = findViewById(R.id.taskList);
        deadlineTask = findViewById(R.id.deadlineTaskText);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        if (userId != -1)
        {
            loadUserTasks(userId);
        }
        else
        {
            Toast.makeText(this, "User data could not be found", Toast.LENGTH_SHORT).show();
        }

        taskList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                intent.putStringArrayListExtra("task_list", taskListArray);
                intent.putExtra("id_user", userId);
                startActivity(intent);
            }
        });

        deadlineTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long millis = System.currentTimeMillis();
                Date date = new Date(millis);
                SimpleDateFormat hourFormat = new SimpleDateFormat("H", Locale.getDefault());
                int hour = Integer.parseInt(hourFormat.format(date));

                //hour = 5;
                if (hour >= 4 && hour <= 9)
                {
                    Intent intent = new Intent(TaskListActivity.this, TaskTODOActivity.class);
                    intent.putStringArrayListExtra("task_list", taskListArray);
                    intent.putExtra("id_user", userId);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadUserTasks(int userId)
    {
        try
        {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connect = databaseConnection.connectionclass();

            if (connect != null)
            {
                String query =
                        "SELECT t.name FROM USER_TASKS ut " +
                                "JOIN TASKS t ON ut.id_task = t.id_task " +
                                "WHERE ut.id_user = " + userId;

                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                StringBuilder builder = new StringBuilder("List Tasks\n\n");
                taskListArray.clear();

                while (rs.next())
                {
                    String taskName = rs.getString("name");
                    taskListArray.add(taskName);
                    builder.append(taskName).append("\n");
                }
                taskList.setText(builder.toString());
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

    @Override
    protected void onResume()
    {
        super.onResume();
        if (userId != -1)
        {
            loadUserTasks(userId);
        }
    }
}
