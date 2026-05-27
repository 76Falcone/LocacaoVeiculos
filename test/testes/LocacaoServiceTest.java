package testes;

import org.junit.Test;
import static org.junit.Assert.*;

import dao.ILocacaoDAO;
import dao.IUsuarioDAO;
import dao.IVeiculoDAO;
import model.Locacao;
import model.Usuario;
import model.UsuarioBuilder;
import model.Veiculo;
import model.VeiculoBuilder;
import service.LocacaoService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocacaoServiceTest {

    @Test
    public void testRealizarLocacaoSucesso() throws Exception {
        // Mocks simples em memória dos DAOs para desacoplar do banco MySQL
        IUsuarioDAO mockUsuarioDAO = new IUsuarioDAO() {
            @Override
            public void cadastrarUsuario(Usuario u) {}
            @Override
            public void deletarUsuario(Usuario u) {}
            @Override
            public void atualizarUsuario(Usuario u) {}
            @Override
            public Usuario visualizarUsuarioByID(Usuario u) {
                // Simula que o usuário existe
                return new UsuarioBuilder().comId(u.getIdUsuario()).comNomeUsuario("Usuario Teste").build();
            }
            @Override
            public List<Usuario> visualizarTodosUsuarios() {
                return new ArrayList<>();
            }
        };

        IVeiculoDAO mockVeiculoDAO = new IVeiculoDAO() {
            private Veiculo veiculo = new VeiculoBuilder()
                    .comIdVeiculo(1)
                    .comModeloVeiculo("Civic")
                    .comValorDiaria(100.0)
                    .comDisponibilidade(true)
                    .build();

            @Override
            public void cadastrarVeiculo(Veiculo v) {}
            @Override
            public void deletarVeiculo(Veiculo v) {}
            @Override
            public void atualizarVeiculo(Veiculo v) {
                // Simula atualização de disponibilidade
                this.veiculo.setDisponibilidade(v.isDisponibilidade());
            }
            @Override
            public Veiculo visualizarVeiculoByID(Veiculo v) {
                return veiculo;
            }
            @Override
            public List<Veiculo> visualizarTodosVeiculos() {
                List<Veiculo> list = new ArrayList<>();
                list.add(veiculo);
                return list;
            }
        };

        List<Locacao> locacoesSalvas = new ArrayList<>();
        ILocacaoDAO mockLocacaoDAO = new ILocacaoDAO() {
            @Override
            public void cadastrarLocacao(Locacao l) {
                locacoesSalvas.add(l);
            }
            @Override
            public void deletarLocacao(Locacao l) {}
            @Override
            public void atualizarLocacao(Locacao l) {}
            @Override
            public Locacao visualizarLocacaoByID(Locacao l) { return null; }
            @Override
            public List<Locacao> visualizarTodasLocacoes() { return locacoesSalvas; }
        };

        // Instancia o serviço injetando os mocks (DIP)
        LocacaoService service = new LocacaoService(mockLocacaoDAO, mockUsuarioDAO, mockVeiculoDAO);

        // Executa a locação com Seguro de Terceiros (+R$ 40/dia)
        service.realizarLocacao(
                1,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                5,
                "Aeroporto",
                0.0,
                0.0,
                true, // seguroTerceiros
                false // seguroCoberturaTotal
        );

        // Verificações
        assertEquals(1, locacoesSalvas.size());
        Locacao salva = locacoesSalvas.get(0);
        assertEquals(1, salva.getUsuario().getIdUsuario());
        assertEquals(1, salva.getVeiculo().getIdVeiculo());
        assertEquals(150.0, salva.getSeguroLocacao(), 0.001); // 5 dias * R$ 30/dia
        assertEquals(650.0, salva.getValorTotal(), 0.001); // (5 dias * R$ 100/dia) + R$ 150 de seguro
        assertFalse(mockVeiculoDAO.visualizarVeiculoByID(null).isDisponibilidade()); // Veículo deve ficar indisponível
    }
}
