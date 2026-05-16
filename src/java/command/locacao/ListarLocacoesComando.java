package command.locacao;

import command.IComando;
import dao.DAOFactory;
import dao.ILocacaoDAO;
import model.Locacao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Command: retorna todas as locações em JSON
public class ListarLocacoesComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        ILocacaoDAO dao = DAOFactory.getLocacaoDAO();
        List<Locacao> locacoes = dao.visualizarTodasLocacoes();

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < locacoes.size(); i++) {
            Locacao l = locacoes.get(i);
            if (i > 0) json.append(",");
            json.append("{");
            json.append("\"idLocacao\":").append(l.getIdLocacao()).append(",");
            json.append("\"idUsuario\":").append(l.getUsuario().getIdUsuario()).append(",");
            json.append("\"nomeUsuario\":\"")
                    .append(l.getUsuario().getNomeUsuario() != null ? l.getUsuario().getNomeUsuario() : "")
                    .append("\",");
            json.append("\"idVeiculo\":").append(l.getVeiculo().getIdVeiculo()).append(",");
            json.append("\"modeloVeiculo\":\"")
                    .append(l.getVeiculo().getModeloVeiculo() != null ? l.getVeiculo().getModeloVeiculo() : "")
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
