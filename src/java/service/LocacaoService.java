package service;

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
import model.decorator.ItemLocacao;
import model.decorator.LocacaoBase;
import model.decorator.SeguroCoberturaTotal;
import model.decorator.SeguroTerceiros;

import java.time.LocalDate;
import java.util.List;

public class LocacaoService {

    private final ILocacaoDAO locacaoDAO;
    private final IUsuarioDAO usuarioDAO;
    private final IVeiculoDAO veiculoDAO;

    public LocacaoService() {
        this.locacaoDAO = DAOFactory.getLocacaoDAO();
        this.usuarioDAO = DAOFactory.getUsuarioDAO();
        this.veiculoDAO = DAOFactory.getVeiculoDAO();
    }

    public LocacaoService(ILocacaoDAO locacaoDAO, IUsuarioDAO usuarioDAO, IVeiculoDAO veiculoDAO) {
        this.locacaoDAO = locacaoDAO;
        this.usuarioDAO = usuarioDAO;
        this.veiculoDAO = veiculoDAO;
    }

    public void realizarLocacao(
            int idUsuario,
            int idVeiculo,
            LocalDate dataRetirada,
            LocalDate dataEntrega,
            int qtdDias,
            String localRetirada,
            double seguroLocacaoFallback,
            double valorTotalFallback,
            boolean aplicarSeguroTerceiros,
            boolean aplicarSeguroCoberturaTotal
    ) throws Exception {

        // Valida se o usuário existe no banco antes de persistir
        Usuario target = new UsuarioBuilder().comId(idUsuario).build();
        if (usuarioDAO.visualizarUsuarioByID(target).getIdUsuario() == 0) {
            List<Usuario> list = usuarioDAO.visualizarTodosUsuarios();
            if (!list.isEmpty()) {
                idUsuario = list.get(0).getIdUsuario();
            }
        }

        Usuario u = new UsuarioBuilder().comId(idUsuario).build();
        Veiculo vParam = new VeiculoBuilder().comIdVeiculo(idVeiculo).build();

        // Busca o veículo completo no banco para termos o valorDiaria
        Veiculo vAtualizar = veiculoDAO.visualizarVeiculoByID(vParam);

        Locacao locacao = new LocacaoBuilder()
                .paraUsuario(u)
                .comVeiculo(vAtualizar)
                .comDataRetirada(dataRetirada)
                .comDataEntrega(dataEntrega)
                .comQtdDias(qtdDias)
                .comLocalRetirada(localRetirada)
                .build();

        // Padrão Decorator
        ItemLocacao itemLocacao = new LocacaoBase(locacao);

        if (aplicarSeguroTerceiros) {
            itemLocacao = new SeguroTerceiros(itemLocacao, qtdDias);
        }
        if (aplicarSeguroCoberturaTotal) {
            itemLocacao = new SeguroCoberturaTotal(itemLocacao, qtdDias);
        }

        // Caso nenhum dos checkboxes do decorator tenha vindo na requisição,
        // manter compatibilidade com o cálculo antigo vindo do formulário HTML original.
        if (!aplicarSeguroTerceiros && !aplicarSeguroCoberturaTotal) {
            locacao.setSeguroLocacao(seguroLocacaoFallback);
            locacao.setValorTotal(valorTotalFallback);
        } else {
            locacao.setSeguroLocacao(itemLocacao.getValorSeguro());
            locacao.setValorTotal(itemLocacao.getValorTotal());
            System.out.println("Descrição do Aluguel: " + itemLocacao.getDescricao());
        }

        locacaoDAO.cadastrarLocacao(locacao);

        // Marca o veículo como indisponível
        vAtualizar.setDisponibilidade(false);
        veiculoDAO.atualizarVeiculo(vAtualizar);
    }
}
