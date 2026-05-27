package command.usuario;

import command.IComando;
import dao.DAOFactory;
import dao.IUsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Command: retorna todos os usuários em JSON
public class ListarUsuariosComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();
        List<Usuario> usuarios = dao.visualizarTodosUsuarios();

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (i > 0) json.append(",");
            json.append("{");
            json.append("\"id\":").append(u.getIdUsuario()).append(",");
            json.append("\"nome\":\"").append(u.getNomeUsuario()).append("\",");
            json.append("\"cpf\":\"").append(u.getCpfUsuario()).append("\",");
            json.append("\"cnh\":\"").append(u.getCnhUsuario()).append("\",");
            json.append("\"email\":\"").append(u.getEmailUsuario()).append("\",");
            json.append("\"celular\":\"").append(u.getCelularUsuario()).append("\",");
            json.append("\"admin\":").append(u.isAdmin());
            json.append("}");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}
