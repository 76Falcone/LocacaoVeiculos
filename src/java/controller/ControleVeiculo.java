package controller;

import command.IComando;
import command.veiculo.AtualizarVeiculoComando;
import command.veiculo.BuscarVeiculoPorIdComando;
import command.veiculo.CadastrarVeiculoComando;
import command.veiculo.DeletarVeiculoComando;
import command.veiculo.EditarVeiculoComando;
import command.veiculo.ListarVeiculosComando;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControleVeiculo", urlPatterns = { "/ControleVeiculo", "/html/ControleVeiculo" })
public class ControleVeiculo extends HttpServlet {

    private final Map<String, IComando> comandos = new HashMap<>();

    @Override
    public void init() throws ServletException {
        comandos.put("CADASTRAR",     new CadastrarVeiculoComando());
        comandos.put("ATUALIZAR",     new AtualizarVeiculoComando());
        comandos.put("EDITAR",        new EditarVeiculoComando());
        comandos.put("DELETAR",       new DeletarVeiculoComando());
        comandos.put("LISTAR",        new ListarVeiculosComando());
        comandos.put("BUSCAR_POR_ID", new BuscarVeiculoPorIdComando());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacao = request.getParameter("op");

        try {
            if (operacao == null) {
                response.sendRedirect(request.getContextPath() + "/html/listarVeiculos.html");
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