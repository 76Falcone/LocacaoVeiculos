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

// Command: remove um veículo pelo ID
public class DeletarVeiculoComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idVeiculo = Integer.parseInt(request.getParameter("id"));
        Veiculo v = new VeiculoBuilder().comIdVeiculo(idVeiculo).build();

        IVeiculoDAO dao = DAOFactory.getVeiculoDAO();
        dao.deletarVeiculo(v);

        response.sendRedirect(request.getContextPath() + "/sucessoVeiculo.jsp");
    }
}
