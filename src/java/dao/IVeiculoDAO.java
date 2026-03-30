package dao;

import java.sql.SQLException;
import model.Veiculo;
import java.util.List;

/**
 *
 * @author 76Falcone
 */
public interface IVeiculoDAO {
    
    // Metodos para o VeiculoDAO
    void cadastrarVeiculo(Veiculo v)throws ClassNotFoundException, SQLException;
    void deletarVeiculo(int id)throws ClassNotFoundException, SQLException;
    void atualizarVeiculo(Veiculo v)throws ClassNotFoundException, SQLException;
    Veiculo visualizarVeiculoByID(int id)throws ClassNotFoundException, SQLException;
    List<Veiculo> visualizarTodosVeiculos()throws ClassNotFoundException, SQLException;
}
