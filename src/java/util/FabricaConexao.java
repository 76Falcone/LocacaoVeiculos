package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 76Falcone
 */
public class FabricaConexao {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/ProjetoLocacao";
        String USERNAME = "root";
        String PASSWORD = "nico0407";
        
        // Inicia o Driver
        Class.forName(DRIVER);
        // Estabelece Conexao
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
