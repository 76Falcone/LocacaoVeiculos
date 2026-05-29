package dao;

import model.TipoSeguro;
import util.FabricaConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acessa a tabela tipo_seguro e retorna os seguros cadastrados.
 *
 * @author 76Falcone
 */
public class TipoSeguroDAO implements ITipoSeguroDAO {

    /** Retorna todos os seguros disponíveis na tabela tipo_seguro. */
    @Override
    public List<TipoSeguro> listarTodosSeguros() throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement cmd = con.prepareStatement("SELECT id, tipo, valor FROM tipo_seguro ORDER BY id");
        ResultSet rs = cmd.executeQuery();

        List<TipoSeguro> lista = new ArrayList<>();
        while (rs.next()) {
            TipoSeguro ts = new TipoSeguro();
            ts.setId(rs.getInt("id"));
            ts.setTipo(rs.getString("tipo"));
            ts.setValor(rs.getDouble("valor"));
            lista.add(ts);
        }
        con.close();
        return lista;
    }

    /** Busca um seguro específico pelo ID. */
    @Override
    public TipoSeguro buscarPorId(int id) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement cmd = con.prepareStatement("SELECT id, tipo, valor FROM tipo_seguro WHERE id = ?");
        cmd.setInt(1, id);
        ResultSet rs = cmd.executeQuery();

        TipoSeguro ts = null;
        if (rs.next()) {
            ts = new TipoSeguro();
            ts.setId(rs.getInt("id"));
            ts.setTipo(rs.getString("tipo"));
            ts.setValor(rs.getDouble("valor"));
        }
        con.close();
        return ts;
    }
}
