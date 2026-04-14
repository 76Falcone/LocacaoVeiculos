package dao;

import java.sql.SQLException;
import java.util.List;
import model.Locacao;

/**
 *
 * @author 76Falcone
 */
public interface ILocacaoDAO {
    void cadastrarLocacao(Locacao l) throws ClassNotFoundException, SQLException;

    void deletarLocacao(Locacao l) throws ClassNotFoundException, SQLException;

    void atualizarLocacao(Locacao l) throws ClassNotFoundException, SQLException;

    Locacao visualizarLocacaoByID(Locacao l) throws ClassNotFoundException, SQLException;

    List<Locacao> visualizarTodasLocacoes() throws ClassNotFoundException, SQLException;
}
