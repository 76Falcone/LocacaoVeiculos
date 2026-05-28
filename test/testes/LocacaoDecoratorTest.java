package testes;

import org.junit.Test;
import static org.junit.Assert.*;

import model.Locacao;
import model.LocacaoBuilder;
import model.Veiculo;
import model.VeiculoBuilder;
import model.decorator.ItemLocacao;
import model.decorator.LocacaoBase;
import model.decorator.SeguroCoberturaTotal;
import model.decorator.SeguroTerceiros;

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

        ItemLocacao locacaoComSeguro = new SeguroTerceiros(new LocacaoBase(locacao));

        // SeguroTerceiros adiciona 10% sobre o valor base (R$ 500) -> R$ 50
        assertEquals(50.00, locacaoComSeguro.getValorSeguro(), 0.001);
        assertEquals(550.00, locacaoComSeguro.getValorTotal(), 0.001);
    }

    @Test
    public void testLocacaoComSeguroCoberturaTotal() {
        Veiculo veiculo = new VeiculoBuilder()
                .comValorDiaria(100.00)
                .build();

        Locacao locacao = new LocacaoBuilder()
                .comVeiculo(veiculo)
                .comQtdDias(5)
                .build();

        ItemLocacao locacaoComSeguro = new SeguroCoberturaTotal(new LocacaoBase(locacao));

        // SeguroCoberturaTotal adiciona 15% sobre o valor base (R$ 500) -> R$ 75
        assertEquals(75.00, locacaoComSeguro.getValorSeguro(), 0.001);
        assertEquals(575.00, locacaoComSeguro.getValorTotal(), 0.001);
    }
}
