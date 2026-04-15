package dao;

import java.sql.SQLException;
import model.Usuario;

/**
 *
 * @author 76Falcone
 */
public interface ILoginDAO {
    Usuario validarLogin(String email, String senha) throws ClassNotFoundException, SQLException;
}