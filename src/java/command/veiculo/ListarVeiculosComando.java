package command.veiculo;

import command.IComando;
import dao.DAOFactory;
import dao.IVeiculoDAO;
import model.Veiculo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Command: retorna todos os veículos em JSON
public class ListarVeiculosComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        IVeiculoDAO dao = DAOFactory.getVeiculoDAO();
        List<Veiculo> veiculos = dao.visualizarTodosVeiculos();

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < veiculos.size(); i++) {
            Veiculo v = veiculos.get(i);
            if (i > 0) json.append(",");
            json.append("{");
            json.append("\"id\":").append(v.getIdVeiculo()).append(",");
            json.append("\"placa\":\"").append(v.getPlacaVeiculo() != null ? v.getPlacaVeiculo().getValor() : "").append("\",");
            json.append("\"modelo\":\"").append(v.getModeloVeiculo()).append("\",");
            json.append("\"cor\":\"").append(v.getCorVeiculo()).append("\",");
            json.append("\"valorDiaria\":").append(v.getValorDiaria()).append(",");
            json.append("\"funcionalidade\":\"").append(v.getFuncionalidadeVeiculo()).append("\",");
            json.append("\"disponibilidade\":").append(v.isDisponibilidade()).append(",");
            json.append("\"arCondicionado\":").append(v.isArCondicionadoVeiculo()).append(",");
            json.append("\"tipoCambio\":\"").append(v.getTipoCambio()).append("\"");
            json.append("}");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}
