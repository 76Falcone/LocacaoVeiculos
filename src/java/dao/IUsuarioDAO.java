/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Usuario;

/**
 *
 * @author 76Falcone
 */
public interface IUsuarioDAO {
    
        // Metodos para o UsuarioDAO
    void cadastrarVeiculo(Usuario u)throws ClassNotFoundException, SQLException;
    void deletarVeiculo(Usuario u)throws ClassNotFoundException, SQLException;
    void atualizarVeiculo(Usuario u)throws ClassNotFoundException, SQLException;
    Usuario visualizarVeiculoByID(Usuario u)throws ClassNotFoundException, SQLException;
    List<Usuario> visualizarTodosVeiculos()throws ClassNotFoundException, SQLException;
}
