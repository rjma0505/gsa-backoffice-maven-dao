package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    // URL de ligação à base de dados MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/gsa_db";
    private static final String USER = "*****"; // Substituir pelo seu utilizador
    private static final String PASSWORD = "*****"; // Substituir pela sua password

    /**
     * Retorna uma conexão ativa à base de dados.
     */
    public static Connection getConnection() throws SQLException {
        Connection con =  DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Conexão bem-sucedida com a base de dados!");
        return con;
    }

}
