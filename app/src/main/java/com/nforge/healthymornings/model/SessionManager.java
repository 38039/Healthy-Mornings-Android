package com.nforge.healthymornings.model;

// ANDROID
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Healthy Mornings Shared Preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void saveUser(int userId) {
        editor.putInt("USER_DATABASE_ID", userId);
        editor.apply();
    }

    public User getUser() {
        DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
        Connection databaseConnection = databaseConnector.establishDatabaseConnection();

        try {
            int userId = sharedPreferences.getInt("USER_DATABASE_ID", -1);
            if (userId == -1) throw new Exception("USER_DATABASE_ID == -1 User not found");

            String SQLQuery = "SELECT * FROM users WHERE id_user = ?";
            PreparedStatement selectUserStatement = databaseConnection.prepareStatement(SQLQuery);
            selectUserStatement.setInt(1, userId);

            ResultSet selectUserResults = selectUserStatement.executeQuery();

        } catch(Exception e) { Log.e("SessionManager", "getUser(): " + e.getMessage());}

        return null;
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
