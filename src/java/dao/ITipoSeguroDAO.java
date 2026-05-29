package dao;

import model.TipoSeguro;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de acesso aos dados de tipo_seguro.
 *
 * @author 76Falcone
 */
public interface ITipoSeguroDAO {

    /** Retorna todos os seguros cadastrados na tabela tipo_seguro. */
    List<TipoSeguro> listarTodosSeguros() throws ClassNotFoundException, SQLException;

    /** Busca um seguro específico pelo ID. */
    TipoSeguro buscarPorId(int id) throws ClassNotFoundException, SQLException;
}
