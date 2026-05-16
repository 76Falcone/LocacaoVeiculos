package command.veiculo;

import command.IComando;
import dao.DAOFactory;
import dao.IVeiculoDAO;
import model.Veiculo;
import model.VeiculoBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Command: atualiza os dados de um veículo existente
public class AtualizarVeiculoComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String idParam = request.getParameter("id");
        if (idParam == null) {
            idParam = request.getParameter("idVeiculo");
        }
        int idVeiculo = Integer.parseInt(idParam);

        String placa = request.getParameter("placa").replaceAll("[^A-Za-z0-9]", "");
        String modelo = request.getParameter("modelo");
        String cor = request.getParameter("cor");
        double valorDiaria = Double.parseDouble(request.getParameter("valorDiaria"));
        String funcionalidade = request.getParameter("funcionalidade");
        boolean arCondicionado = "true".equals(request.getParameter("arCondicionado"));
        boolean disponibilidade = "true".equals(request.getParameter("disponibilidade"));
        String cambio = request.getParameter("tipoCambio");

        Veiculo v = new VeiculoBuilder()
                .comIdVeiculo(idVeiculo)
                .comPlacaVeiculo(placa)
                .comModeloVeiculo(modelo)
                .comCorVeiculo(cor)
                .comValorDiaria(valorDiaria)
                .comFuncionalidadeVeiculo(funcionalidade)
                .comArCondicionadoVeiculo(arCondicionado)
                .comDisponibilidade(disponibilidade)
                .comTipoCambio(cambio)
                .build();

        IVeiculoDAO dao = DAOFactory.getVeiculoDAO();
        dao.atualizarVeiculo(v);

        response.sendRedirect(request.getContextPath() + "/sucessoVeiculo.jsp");
    }
}
