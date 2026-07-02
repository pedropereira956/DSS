package RestaurantesDL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config {
    public final static String URL = "jdbc:mysql://127.0.0.1:3306/ChocapitosRestaurantes";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "guilherme12";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
