// Logika dostępu do danych zadań
package com.nforge.healthymornings.model.repository;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.sql.ResultSet;

import com.nforge.healthymornings.model.data.Task;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.model.utils.SessionManager;


public class TaskRepository {
    private final SessionManager        sessionHandler;
    private DatabaseConnectivityJDBC    databaseConnector   = null;
    private ResultSet                   retrievedTaskData   = null;


    public TaskRepository(Context context) {
        Context currentApplicationContext = context.getApplicationContext();
        sessionHandler = new SessionManager(currentApplicationContext);
    }

    // Wczytuje wszystkie zadania użytkownika (nazwy i identyfikatory) z bazy danych do listy zadań
    public boolean loadUserTasks(
            ArrayList<String> loadedTaskNames,
            ArrayList<Integer> loadedTaskIdentifiers,
            ArrayAdapter<String> adapter
    ) {
        try {
            loadedTaskNames.clear();
            loadedTaskIdentifiers.clear();

            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "SELECT t.name, t.id_task FROM USER_TASKS ut " +
                            "JOIN TASKS t ON ut.id_task = t.id_task " +
                            "WHERE ut.id_user = ?",
                    new Object[]{sessionHandler.getUserSession()}
            );

            if (retrievedTaskData == null) return false;

            while ( retrievedTaskData.next() ) {
                Integer retrievedTaskID = retrievedTaskData.getInt("id_task");
                Log.v("TaskRepository", "loadUserTasks() [Indeks wczytanego zadania]: " + retrievedTaskID);

                String retrievedTaskTitle = retrievedTaskData.getString("name");
                Log.v("TaskRepository", "loadUserTasks() [Nazwa wczytanego zadania]: " + retrievedTaskTitle);

                loadedTaskNames.add(retrievedTaskTitle);
                loadedTaskIdentifiers.add(retrievedTaskID);
            }

            adapter.notifyDataSetChanged();
            return true;
        } catch (Exception taskLoadException) {
            Log.e("TaskRepository", "loadUserTasks(): " + taskLoadException.getMessage());
        } finally { databaseConnector.closeConnection(); }
        return false;
    }

    // Dodaje nowe zadanie o podanych atrybutach do bazy danych
    public boolean addUserTask(
            String taskName,
            String taskCategory,
            String taskDescription,
            int taskPointsReward
    ) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            // Sprawdzanie, czy zadanie o tej samej nazwie już istnieje
            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "SELECT id_task FROM TASKS WHERE name = ?",
                    new Object[]{taskName}
            );

            if (retrievedTaskData != null && retrievedTaskData.next())
                throw new Exception("Zadanie o tej samej nazwie już istnieje");

            // Dodawanie nowego zadania do bazy danych
            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "INSERT INTO TASKS (name, category, description, points_reward) VALUES (?, ?, ?, ?) RETURNING id_task;",
                    new Object[]{taskName, taskCategory, taskDescription, taskPointsReward}
            );

            if ( !retrievedTaskData.next() )
                throw new Exception("Nie udało się przesłać zadania do bazy danych");

            // Dodawanie relacji między nowym zadaniem a użytkownikiem który je stworzył
            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "INSERT INTO user_tasks(id_user, id_task, status) VALUES (?, ?, ?::task_status);",
                    new Object[]{ sessionHandler.getUserSession(),
                                  retrievedTaskData.getInt("id_task"),
                                  "pending"}
            ); // W BAZIE DANYCH STATUS ZAPISYWANY JEST JAKO ENUM Z (DONE, PENDING, SKIPPED) A NIE JAKO STRING

            return true;

        } catch (Exception taskInsertException) {
            Log.e("TaskRepository", "addUserTask(): " + taskInsertException.getMessage());
        } finally { databaseConnector.closeConnection(); }
        return false;
    }

    // Zwraca informacje o zadaniu o podanym ID
    public Task getTaskById(int taskID) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            // Szukanie zadania w bazie danych
            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM TASKS WHERE id_task = ?",
                    new Object[] { taskID }
            );

            // Zwracanie danych zadania
            if (retrievedTaskData != null && retrievedTaskData.next()) {
                return new Task(
                        retrievedTaskData.getInt("id_task"),
                        retrievedTaskData.getString("category"),
                        retrievedTaskData.getString("name"),
                        retrievedTaskData.getString("description"),
                        retrievedTaskData.getInt("points_reward")
                );
            } else throw new Exception("Zadanie o podanym ID nie istnieje");

        } catch (Exception taskEditException) {
            Log.e("TaskRepository", "editUserTask(): " + taskEditException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return null;
    }

    public boolean editTask(
            int ID,
            String name,
            String category,
            String description,
            int points
    ) {
        try {
            // Nawiązywanie połączenia z bazą danych
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            // Aktualizacja danych zadania w bazie danych
            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "UPDATE tasks SET name = ?, category = ?, description = ?, points_reward = ? WHERE id_task = ?;",
                    new Object[]{name, category, description, points, ID}
            );

            // Sprawdzanie czy zapytanie nie wyrzuciło komunikatu błędu
            if (retrievedTaskData != null)
                throw new Exception("Nie udało się edytować zadania");

            return true;

        } catch (Exception editTaskException) {
            Log.e("TaskRepository", "editTask(): " + editTaskException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }

    public boolean deleteTask(int ID) {
        try {
            // Nawiązywanie połączenia z bazą danych
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            // Aktualizacja danych zadania w bazie danych
            retrievedTaskData = databaseConnector.executeSQLQuery(
                    "DELETE FROM tasks WHERE id_task = ?;",
                    new Object[]{ID}
            );

            // Sprawdzanie czy zapytanie nie wyrzuciło komunikatu błędu
            if (retrievedTaskData != null)
                throw new Exception("Nie udało się usunąć zadania");

            return true;

        } catch (Exception deleteTaskException) {
            Log.e("TaskRepository", "deleteTask(): " + deleteTaskException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }
}
