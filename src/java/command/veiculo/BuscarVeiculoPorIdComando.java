package command.veiculo;

import command.IComando;
import dao.DAOFactory;
import dao.IVeiculoDAO;
import model.Veiculo;
import model.VeiculoBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Command: busca veículo por ID e encaminha para a tela de edição
public class BuscarVeiculoPorIdComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idVeiculo = Integer.parseInt(request.getParameter("id"));
        Veiculo param = new VeiculoBuilder().comIdVeiculo(idVeiculo).build();

        IVeiculoDAO dao = DAOFactory.getVeiculoDAO();
        Veiculo veiculo = dao.visualizarVeiculoByID(param);

        request.setAttribute("veiculo", veiculo);
        request.getRequestDispatcher("editarVeiculo.jsp").forward(request, response);
    }
}
