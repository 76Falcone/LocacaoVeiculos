package command.seguro;

import command.IComando;
import dao.DAOFactory;
import dao.ITipoSeguroDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Comando para excluir um tipo de seguro pelo ID.
 *
 * @author 76Falcone
 */
public class DeletarTipoSeguroComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int id = Integer.parseInt(request.getParameter("id"));

        ITipoSeguroDAO dao = DAOFactory.getTipoSeguroDAO();
        dao.deletarTipoSeguro(id);

        response.sendRedirect(request.getContextPath() + "/html/listarSeguros.html");
    }
}
