// Logika dostępu do danych użytkownika
package com.nforge.healthymornings.model.repository;

// ANDROID
import android.app.Application;
import android.content.Context;
import android.util.Log;

// HEALTHY MORNINGS
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.model.utils.SessionManager;


public class UserRepository {
    DatabaseConnectivityJDBC databaseConncetor = null;
    java.sql.ResultSet retrievedUserData = null;
    SessionManager sessionHandler = null;
    private final Context context;

    public UserRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean authenticateUser(String email, String password) {
        try {
            databaseConncetor = new DatabaseConnectivityJDBC();
            databaseConncetor.establishDatabaseConnection();

            retrievedUserData = databaseConncetor.executeSQLQuery(
                    "SELECT * FROM users WHERE email = ? AND password = ?",
                    new Object[]{email, password}
            );

            if (retrievedUserData != null && retrievedUserData.next()) {
                sessionHandler = new SessionManager(context);
                if(!sessionHandler.saveUser( retrievedUserData.getInt("id_user") ))
                    throw new Exception("NIE UDAŁO SIĘ ZAPISAĆ SESJI");
                return true;
            }

        } catch (Exception authenticationException) {
            Log.e("UserRepository", "authenticateUser(): " + authenticationException.getMessage());
        } finally {
            try {
                retrievedUserData.close();
                databaseConncetor.closeConnection();
            } catch (Exception e) {} // TODO
        }

        return false;
    }

    // Zwracanie danych użytkownika na podstawie jego identyfikatora
    public User getUser() {
        try {
            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserID() == -1)
                throw new Exception("UŻYTKOWNIK NIE JEST ZALOGOWANY");

            // Nawiązanie połączenia z bazą
            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Szukanie użytkownika w bazie
            java.sql.ResultSet retrievedUserData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE id_user = ?",
                    new Object[]{sessionHandler.getUserID()}
            );

            // Zwracanie danych użytkownika
            if (retrievedUserData != null && retrievedUserData.next()) {
                User user = new User(
                        retrievedUserData.getInt("id_user"),
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

                // Sprawdzanie czy połączenie z bazą zostało zakończone
                if(!databaseConnector.closeConnection())
                    throw new Exception("POŁĄCZENIE NIE ZOSTAŁO POPRAWNIE ZAKOŃCZONE");

                return user;

            } else throw new Exception("UŻYTKOWNIK NIE ISTNIEJE W BAZIE DANYCH");

        } catch (Exception e) {
            Log.e("SessionManager", "getUser(): " + e.getMessage());
            return null;
        }
    }
}
