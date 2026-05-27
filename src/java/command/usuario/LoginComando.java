package command.usuario;

import command.IComando;
import dao.DAOFactory;
import dao.ILoginDAO;
import dao.IUsuarioDAO;
import model.Usuario;
import util.HashUtil;

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
        String senhaRaw = request.getParameter("senha");
        String senhaHash = HashUtil.sha256(senhaRaw); // Hasheando antes de comparar

        // Verifica no banco de dados
        ILoginDAO loginDao = DAOFactory.getLoginDAO();
        Usuario usuarioValido = loginDao.validarLogin(email, senhaHash);

        if (usuarioValido == null) {
            response.sendRedirect(request.getContextPath() + "/html/login.html?erro=1");
            return;
        }

        HttpSession session = request.getSession();

        String userRole = usuarioValido.isAdmin() ? "admin" : "user";

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
