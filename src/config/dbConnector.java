
package config;
import java.sql.*;

public class dbConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/vrs";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method to get a new database connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method for SELECT queries
    public ResultSet getData(String query) throws SQLException {
    Connection conn = getConnection();
    Statement stmt = conn.createStatement();
    return stmt.executeQuery(query);
    }

    // Method for UPDATE, DELETE, INSERT queries
    public boolean updateData(String query, String... params) {
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                pst.setString(i + 1, params[i]);
            }

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; // Returns true if at least one row is updated

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            return false;
        }
    }
}

