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
    }

    // Deletar Locacao
    @Override
    public void deletarLocacao(Locacao l) throws ClassNotFoundException, SQLException {
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
