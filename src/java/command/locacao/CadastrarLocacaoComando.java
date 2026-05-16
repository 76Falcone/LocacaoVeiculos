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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

        // Valida se o usuário existe no banco antes de persistir
        IUsuarioDAO uDao = DAOFactory.getUsuarioDAO();
        Usuario target = new UsuarioBuilder().comId(idUsuario).build();
        if (uDao.visualizarUsuarioByID(target).getIdUsuario() == 0) {
            List<Usuario> list = uDao.visualizarTodosUsuarios();
            if (!list.isEmpty()) {
                idUsuario = list.get(0).getIdUsuario();
            }
        }

        Usuario u = new UsuarioBuilder().comId(idUsuario).build();
        Veiculo v = new VeiculoBuilder().comIdVeiculo(idVeiculo).build();

        Locacao locacao = new LocacaoBuilder()
                .paraUsuario(u)
                .comVeiculo(v)
                .comDataRetirada(dataRetirada)
                .comDataEntrega(dataEntrega)
                .comQtdDias(qtdDias)
                .comLocalRetirada(localRetirada)
                .comSeguro(seguroLocacao)
                .comValorTotal(valorTotal)
                .build();

        ILocacaoDAO dao = DAOFactory.getLocacaoDAO();
        dao.cadastrarLocacao(locacao);

        // Marca o veículo como indisponível
        IVeiculoDAO veiculoDAO = DAOFactory.getVeiculoDAO();
        Veiculo vAtualizar = veiculoDAO.visualizarVeiculoByID(v);
        vAtualizar.setDisponibilidade(false);
        veiculoDAO.atualizarVeiculo(vAtualizar);

        response.sendRedirect(request.getContextPath() + "/sucessoReserva.jsp");
    }
}
