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

// Command: cadastra um novo veículo
public class CadastrarVeiculoComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String placa = request.getParameter("placa").replaceAll("[^A-Za-z0-9]", "");
        String modelo = request.getParameter("modelo");
        String cor = request.getParameter("cor");
        double valorDiaria = Double.parseDouble(request.getParameter("valorDiaria"));
        String funcionalidade = request.getParameter("funcionalidade");
        boolean arCondicionado = "true".equals(request.getParameter("arCondicionado"));
        boolean disponibilidade = "true".equals(request.getParameter("disponibilidade"));
        String cambio = request.getParameter("tipoCambio");

        Veiculo v = new VeiculoBuilder()
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
        dao.cadastrarVeiculo(v);

        response.sendRedirect(request.getContextPath() + "/sucessoVeiculo.jsp");
    }
}
