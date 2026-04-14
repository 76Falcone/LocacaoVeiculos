package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.FabricaConexao;

/**
 *
 * @author 76Falcone
 */

public class LoginDAO implements ILoginDAO {

    @Override
    public boolean validarLogin(String email, String senha) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("SELECT * FROM usuarios WHERE email = ? AND senha = ?");
        comando.setString(1, email);
        comando.setString(2, senha);
        ResultSet resultado = comando.executeQuery();
        boolean acessoLiberado = false;
        if (resultado.next()) {
            acessoLiberado = true;
        } else {
            acessoLiberado = false;
        }
        con.close();
        return acessoLiberado;
    }
}
