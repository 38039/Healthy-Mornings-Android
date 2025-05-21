package com.nforge.healthymornings.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;

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
    TextView deadlineTask;
    ListView taskList;
    int userId;
    ArrayList<String> taskNameList = new ArrayList<>();
    ArrayList<Integer> taskIdList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        SharedPreferences sharedPreferences = getSharedPreferences("Healthy Mornings Shared Preferences", Context.MODE_PRIVATE);

        taskList = findViewById(R.id.TasksList);
        deadlineTask = findViewById(R.id.deadlineTaskText);
        Button taskAdd = findViewById(R.id.GoToAddTaskActivityButton);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskNameList);
        taskList.setAdapter(adapter);

        userId = sharedPreferences.getInt("userId", -1);
        Log.v("TaskListActivity", "userId: " + userId);

        loadUserTasks(userId);

        taskAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, TaskAddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long listObjectId) {
                Log.v("HelloListView", "You clicked Item: " + listObjectId + " at position:" + index);

                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                intent.putExtra("task_id", taskIdList.get(index));
                intent.putExtra("task_name", taskNameList.get(index));

                startActivity(intent);
                finish();
            }
        });

        deadlineTask.setOnClickListener(new View.OnClickListener() {
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
                    intent.putStringArrayListExtra("task_list", taskNameList);
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
            DatabaseConnectivityJDBC databaseConnectivityJDBC = new DatabaseConnectivityJDBC();
            connect = databaseConnectivityJDBC.establishDatabaseConnection();

            if (connect != null)
            {
                String query =
                        "SELECT t.name, t.id_task FROM USER_TASKS ut " +
                                "JOIN TASKS t ON ut.id_task = t.id_task " +
                                "WHERE ut.id_user = " + userId;

                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                taskNameList.clear();
                taskIdList.clear();

                while (rs.next())
                {
                    String taskName = rs.getString("name");
                    Log.v("TaskListActivity", "taskName: " + taskName);
                    Integer taskId = rs.getInt("id_task");
                    Log.v("TaskListActivity", "taskId: " + taskId);
                    taskNameList.add(taskName);
                    taskIdList.add(taskId);
                }
                adapter.notifyDataSetChanged();

                rs.close();
                st.close();
                connect.close();
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

    // ?
    @Override
    protected void onResume() {
        super.onResume();
        if (userId != -1) loadUserTasks(userId);
    }
}
