package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=quanlycuahang;instance=SQLSERVER;encrypt=true;TrustServerCertificate=true;";
    private static final String USER = "sa"; // Thay bằng tài khoản SQL Server của bạn
    private static final String PASSWORD = "123456789"; // Thay bằng mật khẩu SQL Server của bạn

    private static Connection connection = null;

    /**
     * Lấy kết nối đến cơ sở dữ liệu
     * @return Connection object
     * @throws SQLException nếu không thể kết nối
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Đảm bảo driver JDBC được nạp
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // Thiết lập kết nối
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Không tìm thấy driver JDBC: " + e.getMessage());
            } catch (SQLException e) {
                throw new SQLException("Không thể kết nối đến cơ sở dữ liệu: " + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Đóng kết nối cơ sở dữ liệu
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}