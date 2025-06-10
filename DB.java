import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    static final String URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
    static final String DEFAULT_DB = "flingo";
    static final String USER = "root";      // set your MySQL username
    static final String PASS = "hello";  // set your MySQL password

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        conn.setCatalog(DEFAULT_DB);
        return conn;
    }
}