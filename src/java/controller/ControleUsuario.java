package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import dao.UsuarioDAO;
import dao.LoginDAO;
import model.Usuario;

@WebServlet(name = "ControleUsuario", urlPatterns = {"/ControleUsuario", "/html/ControleUsuario"})
public class ControleUsuario extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacao = request.getParameter("op");

        try {
            UsuarioDAO dao = new UsuarioDAO();

            if (operacao == null) {
                response.sendRedirect(request.getContextPath() + "/html/listarUsuarios.html");

            } else if (operacao.equals("LOGIN")) {
                String email = request.getParameter("email");
                String senha = request.getParameter("senha");

                // Verifica credenciais de administrador (hardcoded)
                if ("admin@gmail.com".equals(email) && "admin123".equals(senha)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioLogado", "admin");
                    session.setAttribute("nomeUsuario", "Administrador");
                    session.setAttribute("role", "admin");

                    // Cookies para o frontend detectar o estado de login
                    Cookie cookieUser = new Cookie("usuarioLogado", "admin");
                    cookieUser.setPath("/");
                    response.addCookie(cookieUser);
                    Cookie cookieName = new Cookie("nomeUsuario", java.net.URLEncoder.encode("Administrador", "UTF-8"));
                    cookieName.setPath("/");
                    response.addCookie(cookieName);
                    Cookie cookieRole = new Cookie("role", "admin");
                    cookieRole.setPath("/");
                    response.addCookie(cookieRole);
                    Cookie cookieId = new Cookie("idUsuario", "0");
                    cookieId.setPath("/");
                    response.addCookie(cookieId);

                    response.sendRedirect(request.getContextPath() + "/html/listarVeiculos.html");
                    return;
                }

                // Verifica no banco de dados (usuário comum)
                LoginDAO loginDao = new LoginDAO();
                Usuario usuarioValido = loginDao.validarLogin(email, senha);

                if (usuarioValido != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioLogado", usuarioValido.getEmailUsuario());
                    session.setAttribute("nomeUsuario", usuarioValido.getNomeUsuario());
                    session.setAttribute("role", "user");

                    // Cookies para o frontend detectar o estado de login
                    Cookie cookieUser = new Cookie("usuarioLogado", usuarioValido.getEmailUsuario());
                    cookieUser.setPath("/");
                    response.addCookie(cookieUser);
                    Cookie cookieName = new Cookie("nomeUsuario", java.net.URLEncoder.encode(usuarioValido.getNomeUsuario(), "UTF-8"));
                    cookieName.setPath("/");
                    response.addCookie(cookieName);
                    Cookie cookieRole = new Cookie("role", "user");
                    cookieRole.setPath("/");
                    response.addCookie(cookieRole);
                    Cookie cookieId = new Cookie("idUsuario", String.valueOf(usuarioValido.getIdUsuario()));
                    cookieId.setPath("/");
                    response.addCookie(cookieId);

                    response.sendRedirect(request.getContextPath() + "/index.html");
                } else {
                    response.sendRedirect(request.getContextPath() + "/html/login.html?erro=1");
                }

            } else if (operacao.equals("CADASTRAR")) {
                String nome = request.getParameter("nome");
                String cpf = request.getParameter("cpf").replaceAll("[^0-9]", "");
                String cnh = request.getParameter("cnh").replaceAll("[^0-9]", "");
                String email = request.getParameter("email");
                String senha = request.getParameter("senha");
                String celular = request.getParameter("celular");

                Usuario u = new Usuario();
                u.setNomeUsuario(nome);
                u.setCpfUsuario(cpf);
                u.setCnhUsuario(cnh);
                u.setEmailUsuario(email);
                u.setSenhaUsuario(senha);
                u.setCelularUsuario(celular);

                dao.cadastrarUsuario(u);
                
                // Redireciona para o INICIO após se cadastrar
                response.sendRedirect(request.getContextPath() + "/index.html"); 

            } else if (operacao.equals("ATUALIZAR")) {
                int idUsuario = Integer.parseInt(request.getParameter("id"));
                String nome = request.getParameter("nome");
                String cpf = request.getParameter("cpf").replaceAll("[^0-9]", "");
                String cnh = request.getParameter("cnh").replaceAll("[^0-9]", "");
                String email = request.getParameter("email");
                String senha = request.getParameter("senha");
                String celular = request.getParameter("celular");

                Usuario u = new Usuario();
                u.setIdUsuario(idUsuario);
                u.setNomeUsuario(nome);
                u.setCpfUsuario(cpf);
                u.setCnhUsuario(cnh);
                u.setEmailUsuario(email);
                u.setSenhaUsuario(senha);
                u.setCelularUsuario(celular);

                dao.atualizarUsuario(u);
                response.sendRedirect(request.getContextPath() + "/sucessoUsuario.jsp");

            } else if (operacao.equals("DELETAR")) {
                int idUsuario = Integer.parseInt(request.getParameter("id"));
                Usuario u = new Usuario();
                u.setIdUsuario(idUsuario);

                dao.deletarUsuario(u);
                response.sendRedirect(request.getContextPath() + "/sucessoUsuario.jsp");

            } else if (operacao.equals("LISTAR")) {
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
                    json.append("\"celular\":\"").append(u.getCelularUsuario()).append("\"");
                    json.append("}");
                }
                json.append("]");
                out.print(json.toString());
                out.flush();

            } else if (operacao.equals("BUSCAR_POR_ID")) {
                int idUsuario = Integer.parseInt(request.getParameter("id"));
                Usuario param = new Usuario();
                param.setIdUsuario(idUsuario);

                Usuario usuario = dao.visualizarUsuarioByID(param);
                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("editarUsuario.jsp").forward(request, response);

            } else if (operacao.equals("LOGOUT")) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                // Remove cookies do frontend
                Cookie cookieUser = new Cookie("usuarioLogado", "");
                cookieUser.setPath("/");
                cookieUser.setMaxAge(0);
                response.addCookie(cookieUser);
                Cookie cookieName = new Cookie("nomeUsuario", "");
                cookieName.setPath("/");
                cookieName.setMaxAge(0);
                response.addCookie(cookieName);
                Cookie cookieRole = new Cookie("role", "");
                cookieRole.setPath("/");
                cookieRole.setMaxAge(0);
                response.addCookie(cookieRole);
                Cookie cookieId = new Cookie("idUsuario", "");
                cookieId.setPath("/");
                cookieId.setMaxAge(0);
                response.addCookie(cookieId);

                response.sendRedirect(request.getContextPath() + "/index.html");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro na operacao " + operacao + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
