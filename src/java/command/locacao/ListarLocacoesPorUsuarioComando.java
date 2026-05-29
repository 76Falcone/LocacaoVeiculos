package command.locacao;

import command.IComando;
import dao.DAOFactory;
import dao.ILocacaoDAO;
import model.Locacao;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command: retorna em JSON apenas as locações do usuário autenticado.
 * Lê o idUsuario do cookie "idUsuario" definido pelo ControleUsuario no login.
 *
 * @author 76Falcone
 */
public class ListarLocacoesPorUsuarioComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        // ── Lê o idUsuario do cookie de sessão ──────────────────────
        int idUsuario = -1;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("idUsuario".equals(c.getName())) {
                    try {
                        idUsuario = Integer.parseInt(c.getValue());
                    } catch (NumberFormatException ignore) { }
                    break;
                }
            }
        }

        if (idUsuario < 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print("{\"erro\":\"Usuário não autenticado.\"}");
            return;
        }

        // ── Busca todas e filtra pelo idUsuario ──────────────────────
        ILocacaoDAO dao = DAOFactory.getLocacaoDAO();
        List<Locacao> todasLocacoes = dao.visualizarTodasLocacoes();

        final int uid = idUsuario;
        List<Locacao> minhasLocacoes = todasLocacoes.stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getIdUsuario() == uid)
                .collect(Collectors.toList());

        // ── Serializa em JSON ────────────────────────────────────────
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < minhasLocacoes.size(); i++) {
            Locacao l = minhasLocacoes.get(i);
            if (i > 0) json.append(",");
            json.append("{");
            json.append("\"idLocacao\":").append(l.getIdLocacao()).append(",");
            json.append("\"idVeiculo\":").append(l.getVeiculo().getIdVeiculo()).append(",");
            json.append("\"modeloVeiculo\":\"")
                    .append(l.getVeiculo().getModeloVeiculo() != null ? l.getVeiculo().getModeloVeiculo() : "")
                    .append("\",");
            json.append("\"corVeiculo\":\"")
                    .append(l.getVeiculo().getCorVeiculo() != null ? l.getVeiculo().getCorVeiculo() : "")
                    .append("\",");
            json.append("\"qtdDias\":").append(l.getQtdDias()).append(",");
            json.append("\"seguroLocacao\":").append(l.getSeguroLocacao()).append(",");
            json.append("\"localRetirada\":\"")
                    .append(l.getLocalRetirada() != null ? l.getLocalRetirada() : "").append("\",");
            json.append("\"valorTotal\":").append(l.getValorTotal()).append(",");
            json.append("\"dataRetirada\":\"")
                    .append(l.getDataRetirada() != null ? l.getDataRetirada().toString() : "").append("\",");
            json.append("\"dataEntrega\":\"")
                    .append(l.getDataEntrega() != null ? l.getDataEntrega().toString() : "").append("\"");
            json.append("}");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}
