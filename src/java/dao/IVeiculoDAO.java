package dao;

import model.Veiculo;
import java.util.List;

/**
 *
 * @author 76Falcone
 */
public interface IVeiculoDAO {
    
    // Metodos para o VeiculoDAO
    void cadastrarVeiculo(Veiculo v);
    void deletarVeiculo(int id);
    void atualizarVeiculo(Veiculo v);
    Veiculo visualizarVeiculoByID(int id);
    List<Veiculo> visualizarTodosVeiculos();
}
