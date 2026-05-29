package model.decorator;

import model.Locacao;

public class LocacaoBase implements ItemLocacao {
    private Locacao locacao;

    public LocacaoBase(Locacao locacao) {
        this.locacao = locacao;
    }

    @Override
    public String getDescricao() {
        return "Aluguel do veículo "
                + (locacao.getVeiculo().getModeloVeiculo() != null ? locacao.getVeiculo().getModeloVeiculo()
                        : "ID " + locacao.getVeiculo().getIdVeiculo());
    }

    @Override
    public double getValorSeguro() {
        return 0.0;
    }

    @Override
    public double getValorTotal() {
        // Custo base: valor da diária * quantidade de dias
        return locacao.getVeiculo().getValorDiaria() * locacao.getQtdDias();
    }
}
