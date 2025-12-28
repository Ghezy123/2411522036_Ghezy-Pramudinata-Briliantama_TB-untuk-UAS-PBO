import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Konfigurasi Database
    private static final String URL = "jdbc:mysql://localhost:3306/barbershop_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Isi jika ada password database

    public static Connection connect() {
        Connection conn = null;
        try {
            // Register Driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Buat Koneksi
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
           
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
        return conn;
    }
}