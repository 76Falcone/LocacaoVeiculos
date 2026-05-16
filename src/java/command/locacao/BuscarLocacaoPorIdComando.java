package command.locacao;

import command.IComando;
import dao.DAOFactory;
import dao.ILocacaoDAO;
import model.Locacao;
import model.LocacaoBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Command: busca locação por ID e encaminha para a tela de edição
public class BuscarLocacaoPorIdComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idLocacao = Integer.parseInt(request.getParameter("id"));
        Locacao param = new LocacaoBuilder().comId(idLocacao).build();

        ILocacaoDAO dao = DAOFactory.getLocacaoDAO();
        Locacao locacao = dao.visualizarLocacaoByID(param);

        request.setAttribute("locacao", locacao);
        request.getRequestDispatcher("editarReserva.jsp").forward(request, response);
    }
}
