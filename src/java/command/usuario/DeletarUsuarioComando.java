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

// Command: remove um usuário pelo ID
public class DeletarUsuarioComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idUsuario = Integer.parseInt(request.getParameter("id"));
        Usuario u = new UsuarioBuilder().comId(idUsuario).build();

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();
        dao.deletarUsuario(u);

        response.sendRedirect(request.getContextPath() + "/sucessoUsuario.jsp");
    }
}
