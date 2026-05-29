package controller;

import command.IComando;
import command.seguro.CadastrarTipoSeguroComando;
import command.seguro.AtualizarTipoSeguroComando;
import command.seguro.DeletarTipoSeguroComando;
import dao.DAOFactory;
import dao.ITipoSeguroDAO;
import model.TipoSeguro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que expõe e gerencia os tipos de seguro.
 * Suporta o CRUD administrativo (CADASTRAR, ATUALIZAR, DELETAR) via padrão Command,
 * e responde por padrão com listagem JSON para o formulário de reservas.
 *
 * @author 76Falcone
 */
@WebServlet(name = "ControleTipoSeguro", urlPatterns = {"/ControleTipoSeguro", "/html/ControleTipoSeguro"})
public class ControleTipoSeguro extends HttpServlet {

    private final Map<String, IComando> comandos = new HashMap<>();

    @Override
    public void init() throws ServletException {
        comandos.put("CADASTRAR", new CadastrarTipoSeguroComando());
        comandos.put("ATUALIZAR", new AtualizarTipoSeguroComando());
        comandos.put("DELETAR",   new DeletarTipoSeguroComando());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String op = request.getParameter("op");

        // Se houver comando mapeado, delega a execução
        if (op != null && comandos.containsKey(op)) {
            try {
                comandos.get(op).executar(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensagem", "Erro ao processar operação de seguro: " + e.getMessage());
                request.getRequestDispatcher("/erro.jsp").forward(request, response);
            }
            return;
        }

        // Caso contrário, executa o comportamento padrão: Listagem JSON para o front-end
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        PrintWriter out = response.getWriter();

        try {
            ITipoSeguroDAO dao = DAOFactory.getTipoSeguroDAO();
            List<TipoSeguro> seguros = dao.listarTodosSeguros();

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < seguros.size(); i++) {
                TipoSeguro ts = seguros.get(i);
                json.append("{")
                    .append("\"id\":").append(ts.getId()).append(",")
                    .append("\"tipo\":\"").append(escaparJson(ts.getTipo())).append("\",")
                    .append("\"valor\":").append(ts.getValor())
                    .append("}");
                if (i < seguros.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"erro\":\"Erro ao carregar seguros: " + escaparJson(e.getMessage()) + "\"}");
        }

        out.flush();
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

    /** Escapa caracteres especiais para JSON seguro. */
    private String escaparJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
