package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Locacao;
import model.Usuario;
import model.Veiculo;
import util.FabricaConexao;

/**
 *
 * @author 76Falcone
 */
public class LocacaoDAO implements ILocacaoDAO {

    // Cadastrar Locacao
    @Override
    public void cadastrarLocacao(Locacao l) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement(
                "insert into locacao (id_usuario, id_veiculo, qtdDias, seguro, localRetirada, valorTotal, data_retirada, data_entrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        comando.setInt(1, l.getUsuario().getIdUsuario());
        comando.setInt(2, l.getVeiculo().getIdVeiculo());
        comando.setInt(3, l.getQtdDias());
        comando.setDouble(4, l.getSeguroLocacao());
        comando.setString(5, l.getLocalRetirada());
        comando.setDouble(6, l.getValorTotal());
        comando.setDate(7, Date.valueOf(l.getDataRetirada()));

        if (l.getDataEntrega() != null) {
            comando.setDate(8, Date.valueOf(l.getDataEntrega()));
        } else {
            comando.setNull(8, java.sql.Types.DATE);
        }

        comando.execute();
        con.close();
    }

    // Deletar Locacao
    @Override
    public void deletarLocacao(Locacao l) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("delete from locacao where id = ?");
        comando.setInt(1, l.getIdLocacao());
        comando.execute();
        con.close();
    }

    // Atualizar Locacao
    @Override
    public void atualizarLocacao(Locacao l) throws ClassNotFoundException, SQLException {
    }

    // Buscar por ID
    @Override
    public Locacao visualizarLocacaoByID(Locacao l) throws ClassNotFoundException, SQLException {
        return new Locacao();
    }

    // Buscar todas
    @Override
    public List<Locacao> visualizarTodasLocacoes() throws ClassNotFoundException, SQLException {
        return new ArrayList<Locacao>();
    }
}
