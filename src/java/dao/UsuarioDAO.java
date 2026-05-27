package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import util.FabricaConexao;

/**
 *
 * @author 76Falcone
 */
public class UsuarioDAO implements IUsuarioDAO {

    // Cadastrar usuario
    @Override
    public void cadastrarUsuario(Usuario u) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement(
                "insert into usuarios (nome, cpf, cnh, email, senha, celular, userAdmin) VALUES (?, ?, ?, ?, ?, ?, ?)");
        comando.setString(1, u.getNomeUsuario());
        comando.setString(2, u.getCpfUsuario());
        comando.setString(3, u.getCnhUsuario());
        comando.setString(4, u.getEmailUsuario());
        comando.setString(5, u.getSenhaUsuario());
        comando.setString(6, u.getCelularUsuario());
        comando.setBoolean(7, u.isAdmin());
        comando.execute();
        con.close();
    }

    // Deletar usuario
    @Override
    public void deletarUsuario(Usuario u) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("delete from usuarios where id = ?");
        comando.setInt(1, u.getIdUsuario());
        comando.execute();
        con.close();
    }

    // Atualizar usuario
    @Override
    public void atualizarUsuario(Usuario u) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement(
                "update usuarios set nome = ?, cpf = ?, cnh = ?, email = ?, senha = ?, celular = ?, userAdmin = ? where id = ?");
        comando.setString(1, u.getNomeUsuario());
        comando.setString(2, u.getCpfUsuario());
        comando.setString(3, u.getCnhUsuario());
        comando.setString(4, u.getEmailUsuario());
        comando.setString(5, u.getSenhaUsuario());
        comando.setString(6, u.getCelularUsuario());
        comando.setBoolean(7, u.isAdmin());
        comando.setInt(8, u.getIdUsuario());
        comando.execute();
        con.close();
    }

    // Buscar por ID
    @Override
    public Usuario visualizarUsuarioByID(Usuario u) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("select * from usuarios where id = ?");
        comando.setInt(1, u.getIdUsuario());
        ResultSet rs = comando.executeQuery();
        Usuario user = new Usuario();

        if (rs.next()) {
            user.setIdUsuario(rs.getInt("id"));
            user.setNomeUsuario(rs.getString("nome"));
            user.setCpfUsuario(rs.getString("cpf"));
            user.setCnhUsuario(rs.getString("cnh"));
            user.setEmailUsuario(rs.getString("email"));
            user.setSenhaUsuario(rs.getString("senha"));
            user.setCelularUsuario(rs.getString("celular"));
            user.setAdmin(rs.getBoolean("userAdmin"));
        }
        con.close();
        return user;
    }

    // Metodo de buscar todos
    @Override
    public List<Usuario> visualizarTodosUsuarios() throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("select * from usuarios");
        ResultSet rs = comando.executeQuery();

        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        while (rs.next()) {
            Usuario user = new Usuario();
            user.setIdUsuario(rs.getInt("id"));
            user.setNomeUsuario(rs.getString("nome"));
            user.setCpfUsuario(rs.getString("cpf"));
            user.setCnhUsuario(rs.getString("cnh"));
            user.setEmailUsuario(rs.getString("email"));
            user.setSenhaUsuario(rs.getString("senha"));
            user.setCelularUsuario(rs.getString("celular"));
            user.setAdmin(rs.getBoolean("userAdmin"));
            listaUsuario.add(user);
        }
        con.close();
        return listaUsuario;
    }

}
