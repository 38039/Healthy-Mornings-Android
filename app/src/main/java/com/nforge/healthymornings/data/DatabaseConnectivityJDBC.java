package com.nforge.healthymornings.data;

// ANDROID
import android.os.StrictMode;
import android.util.Log;

// JDBC
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DatabaseConnectivityJDBC {
    Connection databaseConnection = null;   // Przechowuje połączenie z bazą danych
    PreparedStatement sqlStatement = null;  // Przechowuje zapytanie do bazy danych
    ResultSet sqlResponse = null;           // Przechowuje odpowiedź z bazy danych
    boolean wasQueryBefore = false;         // Przechowuje informację czy zapytanie było już wcześniej wykonane


    // Domyślne dane naszego prywatnego serwera z bazą danych
    String databaseConnectionURL = null,
            databaseName = "healthy_mornings",
            databaseIpAddress = "136.244.87.135",
            databasePort = "5432",
            databaseUsername = "application_jdbc_connection",
            databasePassword = "123",
            databaseSchema = "application";


    // Programista może nadpisać dane połączenia poniższym setterem
    public void provideDatabaseConnectionDetails (
            String providedName,
            String providedIpAddress,
            String providedPort,
            String providedUsername,
            String providedPassword,
            String providedSchema
    ) {
        this.databaseName       = providedName;
        this.databaseIpAddress  = providedIpAddress;
        this.databasePort       = providedPort;
        this.databaseUsername   = providedUsername;
        this.databasePassword   = providedPassword;
        this.databaseSchema     = providedSchema;

        Log.v("DatabaseConnectivityJDBC", "provideDatabaseConnectionDetails(): Connection details: " +
                        "Name: "        + databaseName          + " " +
                        "IP Address: "  + databaseIpAddress     + " " +
                        "Port: "        + databasePort          + " " +
                        "Username: "    + databaseUsername      + " " +
                        "Password: "    + databasePassword      + " " +
                        "Schema: "      + databaseSchema
                );
    }

    // Utworzenie połączenia z bazą danych przez JDBC
    public Connection establishDatabaseConnection() {
        // Strict Mode (Wymagane do poprawnego działania JDBC pod Androidem)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            // Używamy PostgreSQL jako naszej bazy danych
            Class.forName("org.postgresql.Driver");

            // Konkatenacja URL'a (widziałem wersję z danymi logowania, ale tu nie działa prawdopodobnie przez hash)
            databaseConnectionURL =
                    "jdbc:postgresql://"
                            + databaseIpAddress  + ":"
                            + databasePort       + "/"
                            + databaseName
                            + "?currentSchema=" + databaseSchema;

            Log.v("DatabaseConnectivityJDBC ", "CONNECTION URL: " + databaseConnectionURL);


            // Utworzenie połączenia z bazą danych
            databaseConnection = DriverManager.getConnection(
                    databaseConnectionURL,
                    databaseUsername,
                    databasePassword
            );

        } catch (Exception connectionException) {
            Log.e("DatabaseConnectivityJDBC", "establishDatabaseConnection(): " + connectionException.getMessage());
            return null;
        }

        return databaseConnection;
    }

    public ResultSet executeSQLQuery(String query, Object[] arguments) {
        try {
            // Utworzenie zapytania
            sqlStatement = databaseConnection.prepareStatement(query);
            Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL query: " + query);

            // Nadpisanie parametrów zapytania
            for (int i = 1; i <= arguments.length; i++) {
                sqlStatement.setObject(i, arguments[i - 1]);
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL argument: " + arguments[i - 1].toString());
            }

            // Wykonaj / zaktualizuj zapytanie
            if (wasQueryBefore) {
                sqlStatement.executeUpdate();
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL query updated");
                return sqlResponse;
            }

            sqlResponse = sqlStatement.executeQuery();
            Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL query executed");

        } catch (Exception sqlException) {
            Log.e("DatabaseConnectivityJDBC", "executeSQLQuery(): " + sqlException.getMessage());
            return null;
        }

        wasQueryBefore = true;
        return sqlResponse;
    }

    // Czyszczenie i zamykanie otwartych połączeń
    public boolean closeConnection() {
        try {
            databaseConnection.close();
            sqlStatement.close();
            sqlResponse.close();
            Log.v("DatabaseConnectivityJDBC", "closeConnection(): Connection closed");
            return true;
        } catch (Exception connectionCloseException) {
            Log.e("DatabaseConnectivityJDBC", "closeConnection(): " + connectionCloseException.getMessage());
            return false;
        }
    }

}
