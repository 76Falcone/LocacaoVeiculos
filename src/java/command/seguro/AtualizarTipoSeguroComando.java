package command.seguro;

import command.IComando;
import dao.DAOFactory;
import dao.ITipoSeguroDAO;
import model.TipoSeguro;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Comando para atualizar um tipo de seguro existente.
 *
 * @author 76Falcone
 */
public class AtualizarTipoSeguroComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int id = Integer.parseInt(request.getParameter("id"));
        String tipo = request.getParameter("tipo");
        double valor = Double.parseDouble(request.getParameter("valor"));

        TipoSeguro ts = new TipoSeguro(id, tipo, valor);

        ITipoSeguroDAO dao = DAOFactory.getTipoSeguroDAO();
        dao.atualizarTipoSeguro(ts);

        response.sendRedirect(request.getContextPath() + "/html/listarSeguros.html");
    }
}
