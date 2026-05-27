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
    
    private final IUsuarioDAO usuarioDAO;
    private final IVeiculoDAO veiculoDAO;

    public LocacaoDAO() {
        this.usuarioDAO = DAOFactory.getUsuarioDAO();
        this.veiculoDAO = DAOFactory.getVeiculoDAO();
    }

    public LocacaoDAO(IUsuarioDAO usuarioDAO, IVeiculoDAO veiculoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.veiculoDAO = veiculoDAO;
    }

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

        comando.setNull(8, java.sql.Types.DATE);
        if (l.getDataEntrega() != null) {
            comando.setDate(8, Date.valueOf(l.getDataEntrega()));
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
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement(
                "update locacao set id_usuario = ?, id_veiculo = ?, qtdDias = ?, seguro = ?, localRetirada = ?, valorTotal = ?, data_retirada = ?, data_entrega = ? where id = ?");
        comando.setInt(1, l.getUsuario().getIdUsuario());
        comando.setInt(2, l.getVeiculo().getIdVeiculo());
        comando.setInt(3, l.getQtdDias());
        comando.setDouble(4, l.getSeguroLocacao());
        comando.setString(5, l.getLocalRetirada());
        comando.setDouble(6, l.getValorTotal());
        comando.setDate(7, Date.valueOf(l.getDataRetirada()));

        comando.setNull(8, java.sql.Types.DATE);
        if (l.getDataEntrega() != null) {
            comando.setDate(8, Date.valueOf(l.getDataEntrega()));
        }

        comando.setInt(9, l.getIdLocacao());
        comando.execute();
        con.close();
    }

    // Buscar por ID
    @Override
    public Locacao visualizarLocacaoByID(Locacao l) throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("select * from locacao where id = ?");
        comando.setInt(1, l.getIdLocacao());
        ResultSet rs = comando.executeQuery();
        Locacao locacao = new Locacao();

        if (rs.next()) {
            locacao.setIdLocacao(rs.getInt("id"));
            locacao.setQtdDias(rs.getInt("qtdDias"));
            locacao.setSeguroLocacao(rs.getDouble("seguro"));
            locacao.setLocalRetirada(rs.getString("localRetirada"));
            locacao.setValorTotal(rs.getDouble("valorTotal"));

            Date dataRetirada = rs.getDate("data_retirada");
            if (dataRetirada != null) locacao.setDataRetirada(dataRetirada.toLocalDate());

            Date dataEntrega = rs.getDate("data_entrega");
            if (dataEntrega != null) locacao.setDataEntrega(dataEntrega.toLocalDate());

            // Busca o usuario pela FK
            Usuario uParam = new Usuario();
            uParam.setIdUsuario(rs.getInt("id_usuario"));
            locacao.setUsuario(usuarioDAO.visualizarUsuarioByID(uParam));

            // Busca o veiculo pela FK
            Veiculo vParam = new Veiculo();
            vParam.setIdVeiculo(rs.getInt("id_veiculo"));
            locacao.setVeiculo(veiculoDAO.visualizarVeiculoByID(vParam));
        }
        con.close();
        return locacao;
    }

    // Buscar todas
    @Override
    public List<Locacao> visualizarTodasLocacoes() throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("select * from locacao");
        ResultSet rs = comando.executeQuery();

        List<Locacao> listaLocacao = new ArrayList<Locacao>();

        while (rs.next()) {
            Locacao locacao = new Locacao();
            locacao.setIdLocacao(rs.getInt("id"));
            locacao.setQtdDias(rs.getInt("qtdDias"));
            locacao.setSeguroLocacao(rs.getDouble("seguro"));
            locacao.setLocalRetirada(rs.getString("localRetirada"));
            locacao.setValorTotal(rs.getDouble("valorTotal"));

            Date dataRetirada = rs.getDate("data_retirada");
            if (dataRetirada != null) locacao.setDataRetirada(dataRetirada.toLocalDate());

            Date dataEntrega = rs.getDate("data_entrega");
            if (dataEntrega != null) locacao.setDataEntrega(dataEntrega.toLocalDate());

            // Busca o usuario pela FK
            Usuario uParam = new Usuario();
            uParam.setIdUsuario(rs.getInt("id_usuario"));
            locacao.setUsuario(usuarioDAO.visualizarUsuarioByID(uParam));

            // Busca o veiculo pela FK
            Veiculo vParam = new Veiculo();
            vParam.setIdVeiculo(rs.getInt("id_veiculo"));
            locacao.setVeiculo(veiculoDAO.visualizarVeiculoByID(vParam));

            listaLocacao.add(locacao);
        }
        con.close();
        return listaLocacao;
    }
}
