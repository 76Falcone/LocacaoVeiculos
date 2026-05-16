package command.usuario;

import command.IComando;
import dao.DAOFactory;
import dao.IUsuarioDAO;
import model.Usuario;
import model.UsuarioBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Command: busca usuário por ID e encaminha para a tela de edição
public class BuscarUsuarioPorIdComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idUsuario = Integer.parseInt(request.getParameter("id"));
        Usuario param = new UsuarioBuilder().comId(idUsuario).build();

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();
        Usuario usuario = dao.visualizarUsuarioByID(param);

        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("editarUsuario.jsp").forward(request, response);
    }
}
