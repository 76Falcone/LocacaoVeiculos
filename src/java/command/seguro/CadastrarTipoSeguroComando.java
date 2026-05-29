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
 * Comando para cadastrar um novo tipo de seguro.
 *
 * @author 76Falcone
 */
public class CadastrarTipoSeguroComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String tipo = request.getParameter("tipo");
        double valor = Double.parseDouble(request.getParameter("valor"));

        TipoSeguro ts = new TipoSeguro();
        ts.setTipo(tipo);
        ts.setValor(valor);

        ITipoSeguroDAO dao = DAOFactory.getTipoSeguroDAO();
        dao.cadastrarTipoSeguro(ts);

        response.sendRedirect(request.getContextPath() + "/html/listarSeguros.html");
    }
}
