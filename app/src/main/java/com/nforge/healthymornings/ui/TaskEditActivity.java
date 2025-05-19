package com.nforge.healthymornings.ui;

// ANDROID
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

// JDBC
import java.sql.*;

// HEALTHY MORNINGS
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;



public class TaskEditActivity extends AppCompatActivity {
    Connection connect;
    Integer taskId;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        EditText inputName      = findViewById(R.id.TaskNameTextView);
        EditText inputCategory  = findViewById(R.id.TaskCategoryTextView);
        EditText inputDescription = findViewById(R.id.TaskDescriptionTextView);
        EditText inputPoints = findViewById(R.id.TaskPointsTextNumber);
        Button editTaskButton        = findViewById(R.id.EditTaskButton);
        Button deleteButton     = findViewById(R.id.DeleteTaskButton);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("task_id", -1);
        String taskName = intent.getStringExtra("task_name");
        Log.v("TaskAddActivity", "taskId: " + taskId);
        Log.v("TaskAddActivity", "taskName: " + taskName);



        // Otwarcie połączenia z bazą danych
        DatabaseConnectivityJDBC databaseConnectivityJDBC = new DatabaseConnectivityJDBC();
        connect = databaseConnectivityJDBC.establishDatabaseConnection();

        try {
            String query = "SELECT * FROM TASKS WHERE id_task = ?";
            PreparedStatement st = connect.prepareStatement(query);
            st.setInt(1, taskId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String category = rs.getString("category");
                String description = rs.getString("description");
                int pointsReward = rs.getInt("points_reward");

                inputName.setText(name);
                inputCategory.setText(category);
                inputDescription.setText(description);
                inputPoints.setText(String.valueOf(pointsReward));


            } else {
                Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception exception) { Log.e("SQL Error", "Query error: " + exception.getMessage()); }



        editTaskButton.setOnClickListener(v -> {
                    try {

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

                        if (editedTaskName.isEmpty()) {
                            Toast.makeText(this, "Enter a name for the task", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (editedTaskCategory.isEmpty()) {
                            Toast.makeText(this, "Enter a category for the task", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (editedTaskDescription.isEmpty()) {
                            Toast.makeText(this, "Enter a description for the task", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (editedTaskPointsRewardStr.isEmpty()) {
                            Toast.makeText(this, "Enter a proper point reward for the task", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int editedTaskPointsReward = Integer.parseInt(editedTaskPointsRewardStr);

                        if (editedTaskPointsReward < 0 ) {
                            Toast.makeText(this, "Task point reward needs to be positive", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String insertTask = "UPDATE tasks SET name = ?, category = ?, description = ?, points_reward = ? WHERE id_task = ?;";
                        PreparedStatement insertStmt = connect.prepareStatement(insertTask);

                        insertStmt.setString(1, editedTaskName);
                        insertStmt.setString(2, editedTaskCategory);
                        insertStmt.setString(3, editedTaskDescription);
                        insertStmt.setInt(4, editedTaskPointsReward);
                        insertStmt.setInt(5, taskId);

                        insertStmt.executeUpdate();

                        insertStmt.close();
                        Intent goBack = new Intent(this, TaskListActivity.class);
                        startActivity(goBack);
                        finish();

                    }
                    catch (Exception e)
                    {
                        Log.e("TasksEditActivity", "Error adding tasks: " + e.getMessage());
                        Toast.makeText(this, "Error adding tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

        });

        deleteButton.setOnClickListener(v -> {
           try {
               String insertTask = "DELETE FROM tasks WHERE id_task = ?;";
               PreparedStatement insertStmt = connect.prepareStatement(insertTask);

               insertStmt.setInt(1, taskId);
               insertStmt.executeUpdate();

               insertStmt.close();
               Intent goBack = new Intent(this, TaskListActivity.class);
               startActivity(goBack);
               finish();
           }
           catch (Exception e)
           {
               Log.e("TasksEditActivity", "Error removing tasks: " + e.getMessage());
               Toast.makeText(this, "Error removing tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
           }

        });
    }
}
