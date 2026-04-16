package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static Connection getConexion() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/biblioteca_db?useSSL=false&serverTimezone=America/El_Salvador&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = "";   // contraseña por defecto de XAMPP
        return DriverManager.getConnection(url, user, pass);
    }
}