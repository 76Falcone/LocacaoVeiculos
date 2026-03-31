package dao;

// Imports
import model.Veiculo;
import util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 76Falcone
 */
public class VeiculoDAO implements IVeiculoDAO{
  
    // Cadastrar Veiculo
    @Override
    public void cadastrarVeiculo(Veiculo v) throws ClassNotFoundException, SQLException{
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("insert into veiculo (placaVeiculo, modeloVeiculo, corVeiculo, valorDiaria, funcionalidadeVeiculo, disponibilidade, arCondicionado, tipoCambio) value (?, ?, ?, ?, ?, ?, ?, ?)");
        comando.setString(1, v.getPlacaVeiculo());
        comando.setString(2, v.getModeloVeiculo());
        comando.setString(3, v.getCorVeiculo());
        comando.setDouble(4, v.getValorDiaria());
        comando.setString(5, v.getFuncionalidadeVeiculo());
        comando.setBoolean(6, v.isDisponibilidade());
        comando.setBoolean(7, v.isArCondicionadoVeiculo());
        comando.setString(8, v.getTipoCambio());
        comando.execute();
        con.close();
    }
    
        
    // Deletar veiculo
    @Override
    public void deletarVeiculo (Veiculo v) throws ClassNotFoundException, SQLException{
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("delete from veiculo where id = ?");
        comando.setInt(1, v.getIdVeiculo());
        comando.execute();
        con.close();
    }
    
    // Atualizar veiculo
    @Override
    public void atualizarVeiculo(Veiculo v)throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("update veiculo set placaVeiculo = ?, modeloVeiculo = ?, corVeiculo = ?, valorDiaria = ?, funcionalidadeVeiculo = ?, disponibilidade = ?, arCondicionado = ?, tipoCambio = ? where id = ?");
        comando.setString(1, v.getPlacaVeiculo());
        comando.setString(2, v.getModeloVeiculo());
        comando.setString(3, v.getCorVeiculo());
        comando.setDouble(4, v.getValorDiaria());
        comando.setString(5, v.getFuncionalidadeVeiculo());
        comando.setBoolean(6, v.isDisponibilidade());
        comando.setBoolean(7, v.isArCondicionadoVeiculo());
        comando.setString(8, v.getTipoCambio());
        comando.setInt(9, v.getIdVeiculo());
        comando.execute();
        con.close();    
    }
    
   // Buscar por ID
    @Override
    public Veiculo visualizarVeiculoByID(Veiculo v)throws ClassNotFoundException, SQLException {
        Connection con = FabricaConexao.getConexao();
        PreparedStatement comando = con.prepareStatement("select * from veiculo where id = ?");
        comando.setInt(1, v.getIdVeiculo());
        ResultSet rs = comando.executeQuery();
        Veiculo produtoVcl = new Veiculo();
        if (rs.next()) {
            produtoVcl.setIdVeiculo(rs.getInt("id"));
            produtoVcl.setPlacaVeiculo(rs.getString("placa"));
            produtoVcl.setModeloVeiculo(rs.getString("modelo"));
            produtoVcl.setCorVeiculo(rs.getString("cor"));
            produtoVcl.setValorDiaria(rs.getDouble("valorDiaria"));
            produtoVcl.setFuncionalidadeVeiculo(rs.getString("funcionalidade"));
            produtoVcl.setDisponibilidade(rs.getBoolean("disponibilidade"));
            produtoVcl.setArCondicionadoVeiculo(rs.getBoolean("arCondicionado"));
            produtoVcl.setTipoCambio(rs.getString("cambio"));
        }
        con.close();
        return produtoVcl;
    }
}
