// Obsługa sesji w SharedPreferences

package com.nforge.healthymornings.model.utils;

// ANDROID
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

// HEALTHY MORNINGS
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;


public class SessionManager {
    private final SharedPreferences         sessionPreferences;
    private final SharedPreferences.Editor  sessionPreferencesEditor;

    public SessionManager(Context context) {
        sessionPreferences          = context.getSharedPreferences(
                "Healthy Mornings Shared Preferences",
                android.content.Context.MODE_PRIVATE);
        sessionPreferencesEditor    = sessionPreferences.edit();
    }

    // Zapisywanie identyfikatora użytkownika do sesji
    public boolean saveUser(int passedUserID) {
        sessionPreferencesEditor.putInt("USER_DATABASE_ID", passedUserID);
        sessionPreferencesEditor.putBoolean("IS_USER_LOGGED_IN", true);

        Log.v("SessionManager", "saveUser(): USER ID: "
                + sessionPreferences.getInt("USER_DATABASE_ID", -1));
        Log.v("SessionManager", "saveUser(): IS USER LOGGED IN: "
                + sessionPreferences.getBoolean("IS_USER_LOGGED_IN", false));

        return sessionPreferencesEditor.commit();
    }

    // Zwracanie identyfikatora użytkownika z sesji
    public int getUserID() {
        return sessionPreferences.getInt("USER_DATABASE_ID", -1);
    }

    // Czyszczenie pamięci trwałej aplikacji
    public boolean clearSession() {
        sessionPreferencesEditor.clear();
        return sessionPreferencesEditor.commit();
    }
}
