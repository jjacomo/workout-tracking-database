package db_lab.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DAOUtils {
    private DAOUtils() {
    }

    public static Connection localMySQLConnection(String database, String username, String password) {
        try {
            String url = "jdbc:mysql://localhost:3307/" + database;
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public static PreparedStatement prepare(Connection connection, String query, Object... values)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            return statement;
        } catch (Exception e) {
            statement.close();
            throw e;
        }
    }
}
