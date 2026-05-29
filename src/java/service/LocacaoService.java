package service;

import dao.DAOFactory;
import dao.ILocacaoDAO;
import dao.ITipoSeguroDAO;
import dao.IUsuarioDAO;
import dao.IVeiculoDAO;
import model.Locacao;
import model.LocacaoBuilder;
import model.TipoSeguro;
import model.Usuario;
import model.UsuarioBuilder;
import model.Veiculo;
import model.VeiculoBuilder;
import model.decorator.ItemLocacao;
import model.decorator.LocacaoBase;
import model.decorator.SeguroPaneEletrica;
import model.decorator.SeguroPneu;
import model.decorator.SeguroTerceiros;
import model.decorator.SeguroVidrosEspelhos;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço responsável por orquestrar a lógica de negócio de locação.
 * Aplica o padrão Decorator para compor os seguros selecionados,
 * buscando os valores diretamente do banco de dados (tabela tipo_seguro).
 *
 * @author 76Falcone
 */
public class LocacaoService {

    private final ILocacaoDAO    locacaoDAO;
    private final IUsuarioDAO    usuarioDAO;
    private final IVeiculoDAO    veiculoDAO;
    private final ITipoSeguroDAO tipoSeguroDAO;

    public LocacaoService() {
        this.locacaoDAO    = DAOFactory.getLocacaoDAO();
        this.usuarioDAO    = DAOFactory.getUsuarioDAO();
        this.veiculoDAO    = DAOFactory.getVeiculoDAO();
        this.tipoSeguroDAO = DAOFactory.getTipoSeguroDAO();
    }

    public LocacaoService(ILocacaoDAO locacaoDAO, IUsuarioDAO usuarioDAO,
                          IVeiculoDAO veiculoDAO, ITipoSeguroDAO tipoSeguroDAO) {
        this.locacaoDAO    = locacaoDAO;
        this.usuarioDAO    = usuarioDAO;
        this.veiculoDAO    = veiculoDAO;
        this.tipoSeguroDAO = tipoSeguroDAO;
    }

    /**
     * Realiza a locação aplicando os decorators de seguro com valores vindos do banco.
     *
     * @param idUsuario       ID do usuário
     * @param idVeiculo       ID do veículo
     * @param dataRetirada    Data de retirada
     * @param dataEntrega     Data de entrega (pode ser null)
     * @param qtdDias         Quantidade de dias
     * @param localRetirada   Local de retirada
     * @param idsSeguros      IDs dos seguros selecionados na tabela tipo_seguro
     */
    public void realizarLocacao(
            int idUsuario,
            int idVeiculo,
            LocalDate dataRetirada,
            LocalDate dataEntrega,
            int qtdDias,
            String localRetirada,
            int[] idsSeguros
    ) throws Exception {

        // Valida se o usuário existe no banco antes de persistir
        Usuario target = new UsuarioBuilder().comId(idUsuario).build();
        if (usuarioDAO.visualizarUsuarioByID(target).getIdUsuario() == 0) {
            List<Usuario> list = usuarioDAO.visualizarTodosUsuarios();
            if (!list.isEmpty()) {
                idUsuario = list.get(0).getIdUsuario();
            }
        }

        Usuario u      = new UsuarioBuilder().comId(idUsuario).build();
        Veiculo vParam = new VeiculoBuilder().comIdVeiculo(idVeiculo).build();

        // Busca o veículo completo no banco para ter o valorDiaria
        Veiculo vAtualizar = veiculoDAO.visualizarVeiculoByID(vParam);

        Locacao locacao = new LocacaoBuilder()
                .paraUsuario(u)
                .comVeiculo(vAtualizar)
                .comDataRetirada(dataRetirada)
                .comDataEntrega(dataEntrega)
                .comQtdDias(qtdDias)
                .comLocalRetirada(localRetirada)
                .build();

        // ── Padrão Decorator ─────────────────────────────────────
        // Começa com o item base (apenas diárias)
        ItemLocacao itemLocacao = new LocacaoBase(locacao);

        if (idsSeguros != null) {
            for (int idSeguro : idsSeguros) {
                TipoSeguro ts = tipoSeguroDAO.buscarPorId(idSeguro);
                if (ts == null) continue;

                // Instancia o Decorator específico para cada tipo de seguro do banco
                switch (ts.getTipo()) {
                    case "Terceiros":
                        // Percentual: 0.10 = +10% sobre o valor total das diárias
                        itemLocacao = new SeguroTerceiros(itemLocacao, ts.getId(), ts.getTipo(), ts.getValor());
                        break;
                    case "Pane Elétrica":
                        itemLocacao = new SeguroPaneEletrica(itemLocacao, ts.getId(), ts.getValor());
                        break;
                    case "Vidros e Espelhos":
                        itemLocacao = new SeguroVidrosEspelhos(itemLocacao, ts.getId(), ts.getValor());
                        break;
                    case "Pneu":
                        itemLocacao = new SeguroPneu(itemLocacao, ts.getId(), ts.getValor());
                        break;
                    default:
                        System.out.println("Tipo de seguro desconhecido: " + ts.getTipo());
                        break;
                }
            }
        }

        locacao.setSeguroLocacao(itemLocacao.getValorSeguro());
        locacao.setValorTotal(itemLocacao.getValorTotal());

        System.out.println("Descrição da Locação: " + itemLocacao.getDescricao());
        System.out.println("Valor do Seguro: R$ " + itemLocacao.getValorSeguro());
        System.out.println("Valor Total: R$ " + itemLocacao.getValorTotal());

        locacaoDAO.cadastrarLocacao(locacao);

        // Marca o veículo como indisponível
        vAtualizar.setDisponibilidade(false);
        veiculoDAO.atualizarVeiculo(vAtualizar);
    }
}
