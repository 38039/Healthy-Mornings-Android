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
    String databaseConnectionURL = null;    // Przechowuje URL dla drivera JDBC
    String databaseName;
    String databaseIpAddress;
    String databasePort;
    String databaseUsername;
    String databasePassword;
    String databaseSchema;


    // Programista może nadpisać dane połączenia poniższym setterem
    public DatabaseConnectivityJDBC (
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

        Log.v("DatabaseConnectivityJDBC", "provideDatabaseConnectionDetails(): CONNECTION DETAILS: \n" +
                "DATABASE NAME: "        + databaseName          + "\n" +
                "HOST IP ADDRESS: "      + databaseIpAddress     + "\n" +
                "DATABASE PORT: "        + databasePort          + "\n" +
                "DATABASE USERNAME: "    + databaseUsername      + "\n" +
                "DATABASE PASSWORD: "    + databasePassword      + "\n" +
                "DATABASE SCHEMA: "      + databaseSchema
        );
    }

    // Domyślne dane naszego prywatnego serwera z bazą danych
    public DatabaseConnectivityJDBC () {
        this.databaseName       = "healthy_mornings";
        this.databaseIpAddress  = "136.244.87.135";
        this.databasePort       = "5432";
        this.databaseUsername   = "application_jdbc_connection";
        this.databasePassword   = "123";
        this.databaseSchema     = "application";

        Log.v("DatabaseConnectivityJDBC", "provideDatabaseConnectionDetails(): CONNECTION DETAILS: \n" +
                "DATABASE NAME: "        + databaseName          + "\n" +
                "HOST IP ADDRESS: "      + databaseIpAddress     + "\n" +
                "DATABASE PORT: "        + databasePort          + "\n" +
                "DATABASE USERNAME: "    + databaseUsername      + "\n" +
                "DATABASE PASSWORD: "    + databasePassword      + "\n" +
                "DATABASE SCHEMA: "      + databaseSchema
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

    // Wykonywanie zapytań do bazy danych i zwracanie odpowiedzi z niej
    public ResultSet executeSQLQuery(String query, Object[] arguments) {
        try {
            // Utworzenie zapytania
            sqlStatement = databaseConnection.prepareStatement(query);
            Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL QUERY: " + query);

            // Nadpisanie parametrów zapytania
            for (int i = 1; i <= arguments.length; i++) {
                sqlStatement.setObject(i, arguments[i - 1]);
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL ARGUMENT: " + arguments[i - 1].toString());
            }

            // Wykonaj / zaktualizuj zapytanie
            if (query.contains("INSERT") || query.contains("UPDATE") || query.contains("DELETE")) {
                sqlStatement.executeUpdate();
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): CONTENT UPDATED");
                return null;
            } else {
                sqlResponse = sqlStatement.executeQuery();
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): SQL QUERY EXECUTED");
            }

            return sqlResponse;

        } catch (Exception sqlException) {
            Log.e("DatabaseConnectivityJDBC", "executeSQLQuery(): " + sqlException.getMessage());
            return null;
        }
    }

    // Czyszczenie i zamykanie otwartych połączeń
    public boolean closeConnection() {
        try {
            databaseConnection.close();
            sqlStatement.close();
            sqlResponse.close();
            Log.v("DatabaseConnectivityJDBC", "closeConnection(): CONNECTION CLOSED");
            return true;

        } catch (Exception connectionCloseException) {
            Log.e("DatabaseConnectivityJDBC", "closeConnection(): " + connectionCloseException.getMessage());
            return false;
        }
    }

}
