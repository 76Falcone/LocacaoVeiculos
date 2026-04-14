package dao;

import java.sql.SQLException;

/**
 *
 * @author 76Falcone
 */
public interface ILoginDAO {
    void validarLogin(String email, String senha) throws ClassNotFoundException, SQLException;
}