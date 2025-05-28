// Tworzy sesję aplikacji niezależną od jej stanu, poprzez przechowywanie danych w pamięci trwałej
package com.nforge.healthymornings.model.utils;

// ANDROID
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


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
    public boolean saveUserSession(int userDatabaseID) {
        sessionPreferencesEditor.putInt("USER_DATABASE_ID", userDatabaseID);
        sessionPreferencesEditor.putBoolean("IS_USER_LOGGED_IN", true);

        return sessionPreferencesEditor.commit();
    }

    // Zwracanie identyfikatora użytkownika z sesji
    public int getUserSession() {
        return sessionPreferences.getInt("USER_DATABASE_ID", -1);
    }

    // Logowanie danych na temat sesji
    public void getSessionInfo() {
        Log.v("SessionManager", "getSessionInfo(): USER ID: "
                + sessionPreferences.getInt("USER_DATABASE_ID", -1));
        Log.v("SessionManager", "getSessionInfo(): IS USER LOGGED IN: "
                + sessionPreferences.getBoolean("IS_USER_LOGGED_IN", false));
    }

    // Czyszczenie pamięci trwałej aplikacji
    public boolean clearSession() {
        sessionPreferencesEditor.clear();
        return sessionPreferencesEditor.commit();
    }
}
