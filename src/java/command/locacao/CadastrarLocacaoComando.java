package command.locacao;

import command.IComando;
import service.LocacaoService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Command: cadastra uma nova locação e marca o veículo como indisponível.
 * Os seguros selecionados são enviados como IDs (seguroId[]) vindos do front-end,
 * que os busca dinamicamente via /ControleTipoSeguro.
 *
 * @author 76Falcone
 */
public class CadastrarLocacaoComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idVeiculo = Integer.parseInt(request.getParameter("idVeiculo"));

        LocalDate dataRetirada = LocalDate.parse(request.getParameter("dataRetirada"));
        LocalDate dataEntrega  = request.getParameter("dataEntrega") != null
                && !request.getParameter("dataEntrega").isEmpty()
                        ? LocalDate.parse(request.getParameter("dataEntrega"))
                        : null;

        int qtdDias = 1;
        try {
            qtdDias = Integer.parseInt(request.getParameter("qtdDias"));
        } catch (NumberFormatException e) { /* mantém 1 */ }

        String localRetirada = request.getParameter("localRetirada");

        // IDs dos seguros selecionados (checkboxes com name="seguroId" e value=id do banco)
        String[] idsStr = request.getParameterValues("seguroId");
        int[] idsSeguros = null;
        if (idsStr != null && idsStr.length > 0) {
            idsSeguros = new int[idsStr.length];
            for (int i = 0; i < idsStr.length; i++) {
                idsSeguros[i] = Integer.parseInt(idsStr[i]);
            }
        }

        LocacaoService service = new LocacaoService();
        service.realizarLocacao(
                idUsuario,
                idVeiculo,
                dataRetirada,
                dataEntrega,
                qtdDias,
                localRetirada,
                idsSeguros
        );

        response.sendRedirect(request.getContextPath() + "/sucessoReserva.jsp");
    }
}
