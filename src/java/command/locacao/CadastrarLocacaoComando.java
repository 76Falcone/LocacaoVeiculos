package command.locacao;

import command.IComando;
import dao.DAOFactory;
import dao.ILocacaoDAO;
import dao.IUsuarioDAO;
import dao.IVeiculoDAO;
import model.Locacao;
import model.LocacaoBuilder;
import model.Usuario;
import model.UsuarioBuilder;
import model.Veiculo;
import model.VeiculoBuilder;

import service.LocacaoService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

// Command: cadastra uma nova locação e marca o veículo como indisponível
public class CadastrarLocacaoComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idVeiculo = Integer.parseInt(request.getParameter("idVeiculo"));
        LocalDate dataRetirada = LocalDate.parse(request.getParameter("dataRetirada"));
        LocalDate dataEntrega = request.getParameter("dataEntrega") != null
                && !request.getParameter("dataEntrega").isEmpty()
                        ? LocalDate.parse(request.getParameter("dataEntrega"))
                        : null;

        int qtdDias = 1;
        try {
            qtdDias = Integer.parseInt(request.getParameter("qtdDias"));
        } catch (NumberFormatException e) { /* mantém 1 */ }

        String localRetirada = request.getParameter("localRetirada");
        double seguroLocacao = Double.parseDouble(request.getParameter("seguroLocacao"));
        double valorTotal = Double.parseDouble(request.getParameter("valorTotal"));

        boolean aplicarSeguroTerceiros = request.getParameter("seguroTerceiros") != null;
        boolean aplicarSeguroCoberturaTotal = request.getParameter("seguroCoberturaTotal") != null;

        LocacaoService service = new LocacaoService();
        service.realizarLocacao(
                idUsuario,
                idVeiculo,
                dataRetirada,
                dataEntrega,
                qtdDias,
                localRetirada,
                seguroLocacao,
                valorTotal,
                aplicarSeguroTerceiros,
                aplicarSeguroCoberturaTotal
        );

        response.sendRedirect(request.getContextPath() + "/sucessoReserva.jsp");
    }
}
