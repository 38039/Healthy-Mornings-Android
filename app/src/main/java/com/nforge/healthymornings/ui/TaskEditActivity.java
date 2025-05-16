package com.nforge.healthymornings.ui;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.nforge.healthymornings.R;
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;

import java.sql.*;
import java.util.ArrayList;

public class TaskEditActivity extends AppCompatActivity
{
    ArrayList<String> taskList;
    ArrayAdapter<String> adapter;
    int userId, selectedPosition = -1;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        taskList = getIntent().getStringArrayListExtra("task_list");
        userId = getIntent().getIntExtra("id_user", -1);
        if (taskList == null) taskList = new ArrayList<>();

        EditText inputTask = findViewById(R.id.inputTask);
        Button addButton = findViewById(R.id.addTaskButton);
        Button deleteButton = findViewById(R.id.deleteTaskButton);
        ListView listView = findViewById(R.id.taskListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, taskList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            listView.setItemChecked(position, true);
        });

        DatabaseConnectivityJDBC databaseConnectivityJDBC = new DatabaseConnectivityJDBC();
        connect = databaseConnectivityJDBC.establishDatabaseConnection();

        addButton.setOnClickListener(v -> {
            String newTask = inputTask.getText().toString().trim();
            if (!newTask.isEmpty())
            {
                if (connect != null)
                {
                    try
                    {
                        int taskId = -1;
                        String checkQuery = "SELECT id_task FROM TASKS WHERE name = ?";
                        PreparedStatement checkStmt = connect.prepareStatement(checkQuery);
                        checkStmt.setString(1, newTask);
                        ResultSet rs = checkStmt.executeQuery();

                        if (rs.next())
                        {
                            taskId = rs.getInt("id_task");
                        }
                        else
                        {
                            String insertTask = "INSERT INTO TASKS (name) VALUES (?) RETURNING id_task";
                            PreparedStatement insertStmt = connect.prepareStatement(insertTask);
                            insertStmt.setString(1, newTask);
                            ResultSet insertRs = insertStmt.executeQuery();

                            if (insertRs.next())
                            {
                                taskId = insertRs.getInt("id_task");
                            }
                            insertRs.close();
                            insertStmt.close();
                        }
                        rs.close();
                        checkStmt.close();

                        String userTaskQuery = "INSERT INTO USER_TASKS (id_user, id_task) VALUES (?, ?)";
                        PreparedStatement userTaskStmt = connect.prepareStatement(userTaskQuery);
                        userTaskStmt.setInt(1, userId);
                        userTaskStmt.setInt(2, taskId);
                        userTaskStmt.executeUpdate();
                        userTaskStmt.close();

                        taskList.add(newTask);
                        adapter.notifyDataSetChanged();
                        inputTask.setText("");

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Error adding tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "No connection to databases", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(TaskEditActivity.this, "Enter a name for the task", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (selectedPosition != -1)
            {
                String taskToRemove = taskList.get(selectedPosition);

                if (connect != null)
                {
                    try
                    {
                        String taskIdQuery = "SELECT id_task FROM TASKS WHERE name = ?";
                        PreparedStatement getTaskIdStmt = connect.prepareStatement(taskIdQuery);
                        getTaskIdStmt.setString(1, taskToRemove);
                        ResultSet rs = getTaskIdStmt.executeQuery();

                        if (rs.next())
                        {
                            int taskId = rs.getInt("id_task");
                            rs.close();
                            getTaskIdStmt.close();

                            String deleteUserTask = "DELETE FROM USER_TASKS WHERE id_user = ? AND id_task = ?";
                            PreparedStatement deleteUserTaskStmt = connect.prepareStatement(deleteUserTask);
                            deleteUserTaskStmt.setInt(1, userId);
                            deleteUserTaskStmt.setInt(2, taskId);
                            deleteUserTaskStmt.executeUpdate();
                            deleteUserTaskStmt.close();

                            String checkUsage = "SELECT COUNT(*) FROM USER_TASKS WHERE id_task = ?";
                            PreparedStatement checkUsageStmt = connect.prepareStatement(checkUsage);
                            checkUsageStmt.setInt(1, taskId);
                            ResultSet usageRs = checkUsageStmt.executeQuery();

                            if (usageRs.next() && usageRs.getInt(1) == 0)
                            {
                                String deleteTask = "DELETE FROM TASKS WHERE id_task = ?";
                                PreparedStatement deleteTaskStmt = connect.prepareStatement(deleteTask);
                                deleteTaskStmt.setInt(1, taskId);
                                deleteTaskStmt.executeUpdate();
                                deleteTaskStmt.close();
                            }
                            usageRs.close();
                            checkUsageStmt.close();

                            taskList.remove(selectedPosition);
                            adapter.notifyDataSetChanged();
                            selectedPosition = -1;
                            listView.clearChoices();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Error deleting tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "No connection to databases", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(TaskEditActivity.this, "Select the task to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
