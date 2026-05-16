package controller;

import command.IComando;
import command.locacao.AtualizarLocacaoComando;
import command.locacao.BuscarLocacaoPorIdComando;
import command.locacao.CadastrarLocacaoComando;
import command.locacao.DeletarLocacaoComando;
import command.locacao.ListarLocacoesComando;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControleLocacao", urlPatterns = { "/ControleLocacao", "/html/ControleLocacao" })
public class ControleLocacao extends HttpServlet {

    private final Map<String, IComando> comandos = new HashMap<>();

    @Override
    public void init() throws ServletException {
        comandos.put("CADASTRAR",     new CadastrarLocacaoComando());
        comandos.put("ATUALIZAR",     new AtualizarLocacaoComando());
        comandos.put("DELETAR",       new DeletarLocacaoComando());
        comandos.put("LISTAR",        new ListarLocacoesComando());
        comandos.put("BUSCAR_POR_ID", new BuscarLocacaoPorIdComando());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacao = request.getParameter("op");

        try {
            if (operacao == null) {
                response.sendRedirect("../index.html");
                return;
            }

            IComando comando = comandos.get(operacao);
            if (comando != null) {
                comando.executar(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operação inválida: " + operacao);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao processar requisição: " + e.getMessage());
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