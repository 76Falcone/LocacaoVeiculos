package command.locacao;

import command.IComando;
import dao.DAOFactory;
import dao.ILocacaoDAO;
import dao.IVeiculoDAO;
import model.Locacao;
import model.LocacaoBuilder;
import model.Veiculo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Command: remove uma locação e libera o veículo
public class DeletarLocacaoComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idLocacao = Integer.parseInt(request.getParameter("id"));
        Locacao param = new LocacaoBuilder().comId(idLocacao).build();

        ILocacaoDAO dao = DAOFactory.getLocacaoDAO();
        Locacao locacaoExistente = dao.visualizarLocacaoByID(param);
        dao.deletarLocacao(param);

        // Libera o veículo ao remover a locação
        if (locacaoExistente.getVeiculo() != null) {
            IVeiculoDAO veiculoDAO = DAOFactory.getVeiculoDAO();
            Veiculo vLiberar = veiculoDAO.visualizarVeiculoByID(locacaoExistente.getVeiculo());
            vLiberar.setDisponibilidade(true);
            veiculoDAO.atualizarVeiculo(vLiberar);
        }

        response.sendRedirect(request.getContextPath() + "/html/listarReservas.html");
    }
}
