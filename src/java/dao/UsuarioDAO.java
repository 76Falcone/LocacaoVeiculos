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
    
    // Deletar usuario
    @Override
    public void deletarUsuario(Usuario u)throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("delete from usuario where id = ?");
        comando.setInt(1, u.getIdUsuario());
        comando.execute();
        con.close();
    }

    // Atualizar usuario
    @Override
    public void atualizarUsuario(Usuario u)throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("update usuario set nomeUsuario = ?, cpfUsuario = ?, cnhUsuario = ?, emailUsuario = ?, senhaUsuario = ?, celularUsuario = ? where id = ?");
        comando.setString(1, u.getNomeUsuario());
        comando.setString(2, u.getCpfUsuario());
        comando.setString(3, u.getCnhUsuario());
        comando.setString(4, u.getEmailUsuario());
        comando.setString(5, u.getSenhaUsuario());
        comando.setString(6, u.getCelularUsuario());
        comando.setInt(7, u.getIdUsuario());
        comando.execute();
        con.close(); 
    }
}
