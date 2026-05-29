package controller;

import command.IComando;
import command.usuario.AtualizarUsuarioComando;
import command.usuario.BuscarUsuarioPorIdComando;
import command.usuario.CadastrarUsuarioComando;
import command.usuario.DeletarUsuarioComando;
import command.usuario.ListarUsuariosComando;
import command.usuario.LoginComando;
import command.usuario.LogoutComando;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControleUsuario", urlPatterns = { "/ControleUsuario", "/html/ControleUsuario" })
public class ControleUsuario extends HttpServlet {

    private final Map<String, IComando> comandos = new HashMap<>();

    @Override
    public void init() throws ServletException {
        comandos.put("LOGIN",         new LoginComando());
        comandos.put("LOGOUT",        new LogoutComando());
        comandos.put("CADASTRAR",     new CadastrarUsuarioComando());
        comandos.put("ATUALIZAR",     new AtualizarUsuarioComando());
        comandos.put("DELETAR",       new DeletarUsuarioComando());
        comandos.put("LISTAR",        new ListarUsuariosComando());
        comandos.put("BUSCAR_POR_ID", new BuscarUsuarioPorIdComando());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacao = request.getParameter("op");

        try {
            if (operacao == null) {
                response.sendRedirect(request.getContextPath() + "/html/listarUsuarios.html");
                return;
            }

            IComando comando = comandos.get(operacao);
            if (comando == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operação inválida: " + operacao);
                return;
            }
            comando.executar(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagem", e.getMessage());
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
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
