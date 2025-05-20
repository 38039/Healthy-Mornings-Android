package com.nforge.healthymornings.model;

// ANDROID
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

// HEALTHY MORNINGS
import com.nforge.healthymornings.data.DatabaseConnectivityJDBC;


public class SessionManager {
    private final SharedPreferences         sessionPreferences;
    private final SharedPreferences.Editor  sessionPreferencesEditor;

    public SessionManager(Activity activity) {
        sessionPreferences          = activity.getSharedPreferences(
                "Healthy Mornings Shared Preferences",
                android.content.Context.MODE_PRIVATE);
        sessionPreferencesEditor    = sessionPreferences.edit();
    }

    // Zapisywanie identyfikatora użytkownika
    public boolean saveUser(int passedUserID) {
        sessionPreferencesEditor.putInt("USER_DATABASE_ID", passedUserID);
        sessionPreferencesEditor.putBoolean("IS_USER_LOGGED_IN", true);

        Log.v("SessionManager", "saveUser(): USER ID: "
                + sessionPreferences.getInt("USER_DATABASE_ID", -1));
        Log.v("SessionManager", "saveUser(): IS USER LOGGED IN: "
                + sessionPreferences.getBoolean("IS_USER_LOGGED_IN", false));

        return sessionPreferencesEditor.commit();
    }

    // Zwracanie danych użytkownika na podstawie jego identyfikatora
    public UserData getUser() {
        try {
            // Sprawdzanie czy użytkownik jest zalogowany
            if (sessionPreferences.getInt("USER_DATABASE_ID", -1) == -1)
                throw new Exception("UŻYTKOWNIK NIE JEST ZALOGOWANY");

            // Nawiązanie połączenia z bazą
            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Szukanie użytkownika w bazie
            java.sql.ResultSet retrievedUserData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE id_user = ?",
                    new Object[] { Integer.parseInt(sessionPreferences.getString("USER_DATABASE_ID", "-1")) }
            );

            // Zwracanie danych użytkownika
            if (retrievedUserData.next()) {
                return new UserData(
                        retrievedUserData.getString("name"),
                        retrievedUserData.getString("surname"),
                        retrievedUserData.getString("gender"),
                        retrievedUserData.getString("username"),
                        retrievedUserData.getString("email"),
                        retrievedUserData.getString("bio"),
                        retrievedUserData.getDate("birth_date"),
                        retrievedUserData.getDouble("height"),
                        retrievedUserData.getDouble("weight"),
                        retrievedUserData.getBoolean("is_admin")
                );
            } else throw new Exception("UŻYTKOWNIK NIE ISTNIEJE W BAZIE DANYCH");
        } catch(Exception e) { Log.e("SessionManager", "getUser(): " + e.getMessage()); }

        return null;
    }

    // Czyszczenie pamięci trwałej aplikacji
    public boolean clearSession() {
        sessionPreferencesEditor.clear();
        return sessionPreferencesEditor.commit();
    }
}
