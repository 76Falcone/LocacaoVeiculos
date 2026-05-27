package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Usuario;

import util.FabricaConexao;

/**
 *
 * @author 76Falcone
 */

public class LoginDAO implements ILoginDAO {

    @Override
    public Usuario validarLogin(String email, String senha) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("SELECT * FROM usuarios WHERE email = ? AND senha = ?");
        comando.setString(1, email);
        comando.setString(2, senha);
        ResultSet resultado = comando.executeQuery();
        Usuario usuarioLogado = null;
        if (resultado.next()) {
            usuarioLogado = new Usuario();
            usuarioLogado.setIdUsuario(resultado.getInt("id"));
            usuarioLogado.setNomeUsuario(resultado.getString("nome"));
            usuarioLogado.setEmailUsuario(resultado.getString("email"));
            usuarioLogado.setAdmin(resultado.getBoolean("userAdmin"));
        }
        con.close();
        return usuarioLogado;
    }
}
