// Logika dostępu do danych użytkownika
package com.nforge.healthymornings.model.repository;

import android.util.Log;
import android.content.Context;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.services.DatabaseConnectivityJDBC;
import com.nforge.healthymornings.model.utils.SessionManager;


public class UserRepository {
    private final Context               currentApplicationContext;
    private DatabaseConnectivityJDBC    databaseConnector;
    private SessionManager              sessionHandler;
    private ResultSet                   retrievedUserData; // Nie powinien być zamykany

    public UserRepository(Context context) {
        this.currentApplicationContext = context.getApplicationContext();
        sessionHandler = new SessionManager(currentApplicationContext);
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

    public boolean logoutUser() {
        if (!sessionHandler.clearSession()) {
            Log.e("UserViewModel", "logoutUser(): Nie udało się wyczyścić sesji aplikacji");
            return false;
        }

        return true;
    }


    // doesUserExistInDatabase() może przyjąć dowolną liczbę argumentów
    public boolean doesUserExistInDatabase(Map<String, String> columnValueMap) {
        try {
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            StringBuilder queryBuilder = new StringBuilder("SELECT 1 FROM users WHERE ");
            List<Object> values = new ArrayList<>();

            int count = 0;
            for (Map.Entry<String, String> entry : columnValueMap.entrySet()) {
                if (count > 0) {
                    queryBuilder.append(" OR ");
                }
                queryBuilder.append(entry.getKey()).append(" = ?");
                values.add(entry.getValue());
                count++;
            }

            String query = queryBuilder.toString();

            ResultSet rs = databaseConnector.executeSQLQuery(query, values.toArray());

            // Jeśli znajdzie wynik, użytkownik istnieje
            if (rs != null && rs.next()) {
                Log.v("UserRepository", "doesUserExistInDatabase(): użytkownik istnieje.");
                return true;
            }

        } catch (Exception e) {
            Log.w("UserRepository", "doesUserExistInDatabase(): Błąd - " + e.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }



    public boolean registerUserCredentials(String username, String email, String password, Date date_of_birth) {
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
    public User getUserCredentials() {
        try {
            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("UŻYTKOWNIK NIE JEST ZALOGOWANY");

            // Nawiązanie połączenia z bazą
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Szukanie użytkownika w bazie
            retrievedUserData = databaseConnector.executeSQLQuery(
                    "SELECT * FROM users WHERE id_user = ?",
                    new Object[]{sessionHandler.getUserSession()}
            );

            // Zwracanie danych użytkownika
            if (retrievedUserData != null && retrievedUserData.next()) {
                return new User(
                        retrievedUserData.getInt("id_user"),
                        retrievedUserData.getString("name"),
                        retrievedUserData.getString("surname"),
                        retrievedUserData.getString("gender"),
                        retrievedUserData.getString("username"),
                        retrievedUserData.getString("email"),
                        retrievedUserData.getString("password"),
                        retrievedUserData.getString("bio"),
                        retrievedUserData.getDate("date_of_birth"),
                        retrievedUserData.getDouble("height"),
                        retrievedUserData.getDouble("weight"),
                        retrievedUserData.getBoolean("is_admin")
                );

            } else throw new Exception("UŻYTKOWNIK NIE ISTNIEJE W BAZIE DANYCH");

        } catch (Exception e) {
            Log.e("SessionManager", "getUser(): " + e.getMessage());
            return null;
        }
    }

    public boolean changeUserCredentials (
            String          name,
            String          surname,
            String          gender,
            String          username,
            String          email,
            String          bio,
            double          height,
            double          weight
    ) {
        try {
            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            // Nawiązanie połączenia z bazą
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Aktualizacja użytkownika w bazie danych
            retrievedUserData = databaseConnector.executeSQLQuery(
                    "UPDATE users SET name = ?, surname = ?, gender = ?, username = ?, email = ?, bio = ?, height = ?, weight = ? WHERE id_user = ?",
                    new Object[] { name, surname, gender, username, email, bio, height, weight, sessionHandler.getUserSession() }
            );

            if ( retrievedUserData != null )
                throw new Exception("Nie udało się zaktualizować danych użytkownika");

            return true;

        } catch (Exception userCredentialsEditException) {
            Log.e("UserRepository", "changeUserCredentials(): " + userCredentialsEditException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }

    // TODO: Można scalić metody changeUserCredentials i changeUserPassword
    public boolean changeUserPassword(String password) {
        try {
            // Sprawdzanie czy użytkownik jest zalogowany
            if(sessionHandler.getUserSession() == -1)
                throw new Exception("Użytkownik nie jest zalogowany");

            // Nawiązanie połączenia z bazą
            databaseConnector = new DatabaseConnectivityJDBC();
            databaseConnector.establishDatabaseConnection();

            // Aktualizacja użytkownika w bazie danych
            retrievedUserData = databaseConnector.executeSQLQuery(
                    "UPDATE users SET password = ? WHERE id_user = ?",
                    new Object[] { password, sessionHandler.getUserSession() }
            );

            if ( retrievedUserData != null )
                throw new Exception("Nie udało się zaktualizować danych użytkownika");

            return true;

        } catch (Exception userCredentialsEditException) {
            Log.e("UserRepository", "changeUserPassword(): " + userCredentialsEditException.getMessage());
        } finally { databaseConnector.closeConnection(); }

        return false;
    }
}
