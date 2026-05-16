package command.usuario;

import command.IComando;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Command: encerra a sessão e remove os cookies do usuário
public class LogoutComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.addCookie(expireCookie("usuarioLogado"));
        response.addCookie(expireCookie("nomeUsuario"));
        response.addCookie(expireCookie("role"));
        response.addCookie(expireCookie("idUsuario"));

        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    private Cookie expireCookie(String nome) {
        Cookie c = new Cookie(nome, "");
        c.setPath("/");
        c.setMaxAge(0);
        return c;
    }
}
