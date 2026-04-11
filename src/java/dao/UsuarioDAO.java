package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Usuario;
import util.FabricaConexao;

/**
 *
 * @author 76Falcone
 */
public class UsuarioDAO implements IUsuarioDAO{
    
    // Cadastrar usuario
    @Override
    public void cadastrarUsuario(Usuario u)throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("insert into usuarios (nomeUsuario, cpfUsuario, cnhUsuario, emailUsuario, senhaUsuario, celularUsuario) value (?, ?, ?, ?, ?, ?)");
        comando.setString(1, u.getNomeUsuario());
        comando.setString(2, u.getCpfUsuario());
        comando.setString(3, u.getCnhUsuario());
        comando.setString(4, u.getEmailUsuario());
        comando.setString(5, u.getSenhaUsuario());
        comando.setString(6, u.getCelularUsuario());
        comando.execute();
        con.close();
    }
}
