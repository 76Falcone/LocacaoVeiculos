package testes;

import org.junit.Test;
import static org.junit.Assert.*;

import model.Locacao;
import model.LocacaoBuilder;
import model.Veiculo;
import model.VeiculoBuilder;
import model.decorator.ItemLocacao;
import model.decorator.LocacaoBase;
import model.decorator.SeguroTerceiros;
import model.decorator.SeguroPaneEletrica;
import model.decorator.SeguroDinamico;
import model.TipoSeguro;

public class LocacaoDecoratorTest {

    @Test
    public void testLocacaoBaseSemSeguro() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        ItemLocacao base = new LocacaoBase(locacao);

        assertEquals(500.00, base.getValorTotal(), 0.001);
        assertEquals(0.0, base.getValorSeguro(), 0.001);
    }

    @Test
    public void testLocacaoComSeguroTerceiros() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        // SeguroTerceiros(itemDecorado, idSeguro, descricaoSeguro, taxa)
        // taxa = 0.10 = 10%
        ItemLocacao locacaoComSeguro = new SeguroTerceiros(new LocacaoBase(locacao), 1, "Terceiros", 0.10);

        // SeguroTerceiros adiciona 10% sobre o valor base (R$ 500) -> R$ 50
        assertEquals(50.00, locacaoComSeguro.getValorSeguro(), 0.001);
        assertEquals(550.00, locacaoComSeguro.getValorTotal(), 0.001);
    }

    @Test
    public void testLocacaoComSeguroPaneEletrica() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        // SeguroPaneEletrica(itemDecorado, idSeguro, valorFixo)
        ItemLocacao locacaoComSeguro = new SeguroPaneEletrica(new LocacaoBase(locacao), 2, 50.00);

        // SeguroPaneEletrica adiciona valor fixo de R$ 50
        assertEquals(50.00, locacaoComSeguro.getValorSeguro(), 0.001);
        assertEquals(550.00, locacaoComSeguro.getValorTotal(), 0.001);
    }

    @Test
    public void testLocacaoComSeguroDinamicoFixo() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        // Seguro cadastrado dinamicamente no banco: valor >= 1.0 (Fixo)
        TipoSeguro ts = new TipoSeguro(5, "Seguro Incêndio", 80.00);
        ItemLocacao locacaoComSeguro = new SeguroDinamico(new LocacaoBase(locacao), ts);

        assertEquals(80.00, locacaoComSeguro.getValorSeguro(), 0.001);
        assertEquals(580.00, locacaoComSeguro.getValorTotal(), 0.001);
    }

    @Test
    public void testLocacaoComSeguroDinamicoPercentual() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        // Seguro cadastrado dinamicamente no banco: valor < 1.0 (Percentual)
        TipoSeguro ts = new TipoSeguro(6, "Seguro Especial", 0.15);
        ItemLocacao locacaoComSeguro = new SeguroDinamico(new LocacaoBase(locacao), ts);

        // 15% sobre o valor base de R$ 500 = R$ 75
        assertEquals(75.00, locacaoComSeguro.getValorSeguro(), 0.001);
        assertEquals(575.00, locacaoComSeguro.getValorTotal(), 0.001);
    }

    @Test
    public void testLocacaoComMultiplosSeguros() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        // Combina SeguroTerceiros (10% = R$ 50) e SeguroPaneEletrica (Fixo = R$ 30)
        ItemLocacao base = new LocacaoBase(locacao);
        ItemLocacao comTerceiros = new SeguroTerceiros(base, 1, "Terceiros", 0.10);
        ItemLocacao comAmbos = new SeguroPaneEletrica(comTerceiros, 2, 30.00);

        // Valor total deve ser 500 (base) + 50 (Terceiros) + 30 (Pane) = 580
        assertEquals(580.00, comAmbos.getValorTotal(), 0.001);
        
        // Valor do seguro acumulado deve ser 50 + 30 = 80
        assertEquals(80.00, comAmbos.getValorSeguro(), 0.001);
    }
}

