// Logika dostępu do danych użytkownika
package com.nforge.healthymornings.model.repository;

import android.util.Log;
import android.content.Context;

import java.sql.Date;
import java.sql.ResultSet;

import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.model.utils.SessionManager;


public class UserRepository {
    private final Context               currentApplicationContext;
    private DatabaseConnectivityJDBC    databaseConnector       = null;
    private SessionManager              sessionHandler          = null;
    private ResultSet                   retrievedUserData       = null; // Nie powinien być zamykany

    public UserRepository(Context context) {
        this.currentApplicationContext = context.getApplicationContext();
    }


    public boolean authenticateUser(String email, String password) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            retrievedUserData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE email = ? AND password = ?",
                    new Object[]{email, password}
            );


            // Zapis do sesji
            if (retrievedUserData != null && retrievedUserData.next()) {
                sessionHandler = new SessionManager(currentApplicationContext);

                // Sesja ma żyć na czas trwania działania aplikacji, potem być czyszczona
                if ( !sessionHandler.clearSession() )
                    throw new Exception("Nie udało się wyczyścić sesji aplikacji");
                Log.v("UserRepository", "authenticateUser(): Wyczyszczono sesję aplikacji");

                int userID = retrievedUserData.getInt("id_user");
                if(!sessionHandler.saveUserSession( userID ))
                    throw new Exception("Nie udało się zapisać sesji");
                Log.v("UserRepository", "authenticateUser(): Zapisano sesję aplikacji");

                sessionHandler.getSessionInfo();

                return true;
            } else throw new Exception("Użytkownik nie istnieje w bazie danych");

        }
        catch (Exception authenticationException) {
            Log.e("UserRepository", "authenticateUser(): " + authenticationException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }

    public boolean doesUserExistInDatabase(String email, String username) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            retrievedUserData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE email = ? OR username = ?",
                    new Object[]{email, username}
            );

            if(retrievedUserData != null && retrievedUserData.next()) {
                Log.v("UserRepository", "doesUserExistInDatabase(): Użytkownik istnieje w bazie danych");
                return true;
            }

        } catch (Exception userExistsException) {
            Log.w("UserRepository", "doesUserExistInDatabase(): " + userExistsException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }

    public boolean saveUserCredentials(String username, String email, String password, Date date_of_birth) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Wpisz użytkownika do bazy danych
            databaseConnector.executeSQLQuery(
                    "INSERT INTO users (username, email, password, date_of_birth, is_admin, level) VALUES (?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            username,
                            email,
                            password,
                            date_of_birth,
                            false, // IS_ADMIN
                            1      // LEVEL
                    }
            );

            return true;
        } catch (Exception registrationException) {
            Log.e("UserRepository", "registerUserCredentialsInsideDatabase(): " + registrationException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }




    // Zwracanie danych użytkownika na podstawie jego identyfikatora
    public User getUser() {
        try {
            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("UŻYTKOWNIK NIE JEST ZALOGOWANY");

            // Nawiązanie połączenia z bazą
            DatabaseConnectivityJDBC databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Szukanie użytkownika w bazie
            retrievedUserData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE id_user = ?",
                    new Object[]{sessionHandler.getUserSession()}
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
