package controller;

import dao.DAOFactory;
import dao.ITipoSeguroDAO;
import model.TipoSeguro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que expõe os tipos de seguro do banco como JSON.
 * Usado pelo front-end para montar os cards de seguro dinamicamente.
 *
 * GET /ControleTipoSeguro
 * Retorna: [{"id":1,"tipo":"Terceiros","valor":0.10}, ...]
 *
 * @author 76Falcone
 */
@WebServlet(name = "ControleTipoSeguro", urlPatterns = {"/ControleTipoSeguro", "/html/ControleTipoSeguro"})
public class ControleTipoSeguro extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

    /** Escapa caracteres especiais para JSON seguro. */
    private String escaparJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
