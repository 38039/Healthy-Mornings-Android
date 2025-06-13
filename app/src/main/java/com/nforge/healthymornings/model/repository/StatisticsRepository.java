package com.nforge.healthymornings.model.repository;

import android.content.Context;
import android.util.Log;

import java.sql.ResultSet;

import com.nforge.healthymornings.model.data.Statistics;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.model.utils.SessionManager;

public class StatisticsRepository {
    private final SessionManager sessionHandler;
    private DatabaseConnectivityJDBC databaseConnector = null;
    private ResultSet retrievedStatsData = null;

    public StatisticsRepository(Context context) {
        Context currentApplicationContext = context.getApplicationContext();
        sessionHandler = new SessionManager(currentApplicationContext);
    }

    public Statistics getCurrentUserStatistics() {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            long userId = sessionHandler.getUserSession();
            if (userId == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            retrievedStatsData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM user_statistics WHERE id_user = ?",
                    new Object[]{userId}
            );

            if (retrievedStatsData != null && retrievedStatsData.next()) {
                return new Statistics(
                        retrievedStatsData.getLong("id_statistics"),
                        retrievedStatsData.getLong("id_user"),
                        retrievedStatsData.getShort("tasks_active"),
                        retrievedStatsData.getShort("tasks_completed")
                );
            } else {
                throw new Exception("Nie znaleziono statystyk dla użytkownika o ID: " + userId);
            }

        } catch (Exception e) {
            Log.e("StatisticsRepository", "getCurrentUserStatistics(): " + e.getMessage());
        } finally {
            if (databaseConnector != null) databaseConnector.closeConnection();
        }
        return null;
    }

    // Aktualizuje liczby aktywnych i zakończonych zadań użytkownika
    public boolean updateUserStatistics(short tasksActive, short tasksCompleted) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            long userId = sessionHandler.getUserSession();
            if (userId == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            retrievedStatsData = databaseConnector.executeSQLQuery(
                    "SELECT id_statistics FROM user_statistics WHERE id_user = ?",
                    new Object[]{userId}
            );

            if (retrievedStatsData != null && retrievedStatsData.next()) {
                databaseConnector.executeSQLQuery(
                        "UPDATE user_statistics SET tasks_active = ?, tasks_completed = ? WHERE id_user = ?",
                        new Object[]{tasksActive, tasksCompleted, userId}
                );
            } else {
                databaseConnector.executeSQLQuery(
                        "INSERT INTO user_statistics (id_user, tasks_active, tasks_completed) VALUES (?, ?, ?)",
                        new Object[]{userId, tasksActive, tasksCompleted}
                );
            }

            return true;

        } catch (Exception e) {
            Log.e("StatisticsRepository", "updateUserStatistics(): " + e.getMessage());
        } finally {
            if (databaseConnector != null) databaseConnector.closeConnection();
        }

        return false;
    }
}
