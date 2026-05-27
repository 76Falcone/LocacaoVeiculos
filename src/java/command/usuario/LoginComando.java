package command.usuario;

import command.IComando;
import dao.DAOFactory;
import dao.ILoginDAO;
import dao.IUsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// Command: autentica o usuário e inicia a sessão
public class LoginComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();

        // Credenciais fixas de administrador
        if ("admin@gmail.com".equals(email) && "admin123".equals(senha)) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", "admin");
            session.setAttribute("nomeUsuario", "Administrador");
            session.setAttribute("role", "admin");

            String adminIdToUse = "1";
            List<Usuario> usersExistentes = dao.visualizarTodosUsuarios();
            if (usersExistentes != null && !usersExistentes.isEmpty()) {
                adminIdToUse = String.valueOf(usersExistentes.get(0).getIdUsuario());
            }

            response.addCookie(buildCookie("usuarioLogado", "admin"));
            response.addCookie(buildCookie("nomeUsuario", java.net.URLEncoder.encode("Administrador", "UTF-8")));
            response.addCookie(buildCookie("role", "admin"));
            response.addCookie(buildCookie("idUsuario", adminIdToUse));

            response.sendRedirect(request.getContextPath() + "/html/listarVeiculos.html");
            return;
        }

        // Verifica no banco de dados
        ILoginDAO loginDao = DAOFactory.getLoginDAO();
        Usuario usuarioValido = loginDao.validarLogin(email, senha);

        if (usuarioValido == null) {
            response.sendRedirect(request.getContextPath() + "/html/login.html?erro=1");
            return;
        }

        HttpSession session = request.getSession();

        String userRole = "user";
        if ("falcone0407@gmail.com".equalsIgnoreCase(usuarioValido.getEmailUsuario())) {
            userRole = "admin";
        }

        session.setAttribute("usuarioLogado", usuarioValido.getEmailUsuario());
        session.setAttribute("nomeUsuario", usuarioValido.getNomeUsuario());
        session.setAttribute("role", userRole);

        response.addCookie(buildCookie("usuarioLogado", usuarioValido.getEmailUsuario()));
        response.addCookie(buildCookie("nomeUsuario",
                java.net.URLEncoder.encode(usuarioValido.getNomeUsuario(), "UTF-8")));
        response.addCookie(buildCookie("role", userRole));
        response.addCookie(buildCookie("idUsuario", String.valueOf(usuarioValido.getIdUsuario())));

        if ("admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/html/listarVeiculos.html");
            return;
        }
        
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    private Cookie buildCookie(String nome, String valor) {
        Cookie c = new Cookie(nome, valor);
        c.setPath("/");
        return c;
    }
}
