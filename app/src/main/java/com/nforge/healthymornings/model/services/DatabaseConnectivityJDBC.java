// Obsługa połączenia z bazą danych przez JDBC
package com.nforge.healthymornings.model.services;

import android.util.Log;
import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DatabaseConnectivityJDBC {
    private Connection          databaseConnection          = null;  // Przechowuje połączenie z bazą danych
    private PreparedStatement   sqlStatement                = null;  // Przechowuje zapytanie do bazy danych
    private ResultSet           sqlResponse                 = null;  // Przechowuje odpowiedź z bazy danych
    private final String        databaseName;
    private final String        databaseIpAddress;
    private final String        databasePort;
    private final String        databaseUsername;
    private final String        databasePassword;
    private final String        databaseSchema;


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
            // Używamy PostgreSQL jako DBMS dla naszej bazy danych
            Class.forName("org.postgresql.Driver");

            // Przechowuje URL dla drivera JDBC
            // Konkatenacja URL'a (widziałem wersję z danymi logowania, ale tu nie działa prawdopodobnie przez hash)
            String databaseConnectionURL = "jdbc:postgresql://"
                    + databaseIpAddress + ":"
                    + databasePort + "/"
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
            try {
                databaseConnection.close();
                Log.e("DatabaseConnectivityJDBC", "establishDatabaseConnection(): " + connectionException.getMessage());

                return null;
            } catch (Exception connectionCloseException) {
                Log.e("DatabaseConnectivityJDBC", "establishDatabaseConnection(): " + connectionCloseException.getMessage());
            }
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

            // Flagi w celu określenia czy zapytanie ma coś zwracać
            boolean isReturningInsert = query.trim().toUpperCase().startsWith("INSERT") && query.toUpperCase().contains("RETURNING");
            boolean isSelect = query.trim().toUpperCase().startsWith("SELECT");

            if (isSelect || isReturningInsert) {
                sqlResponse = sqlStatement.executeQuery();
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): QUERY EXECUTED AND RESULT SET RETURNED");
                return sqlResponse;
            } else {
                sqlStatement.executeUpdate(); // Dla INSERT, UPDATE, DELETE bez RETURNING
                Log.v("DatabaseConnectivityJDBC", "executeSQLQuery(): UPDATE EXECUTED (NO RESULT)");
                return null;
            }

        } catch (Exception sqlException) {
            Log.e("DatabaseConnectivityJDBC", "executeSQLQuery(): " + sqlException.getMessage());
            return null;
        }
//        finally {
//            try {
//                sqlResponse.close(); // BUG
//                sqlStatement.close();
//            } catch (Exception sqlResourceCloseException) {
//                Log.e("DatabaseConnectivityJDBC", "executeSQLQuery(): " + sqlResourceCloseException.getMessage());
//            }
//        }
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
