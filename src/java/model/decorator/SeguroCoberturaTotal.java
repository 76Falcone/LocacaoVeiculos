package model.decorator;

public class SeguroCoberturaTotal extends SeguroDecorator {
    private static final double VALOR_DIARIO_SEGURO = 80.00;
    private int qtdDias;

    public SeguroCoberturaTotal(ItemLocacao itemDecorado, int qtdDias) {
        super(itemDecorado);
        this.qtdDias = qtdDias;
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Seguro Cobertura Total";
    }

    @Override
    public double getValorSeguro() {
        return itemDecorado.getValorSeguro() + (VALOR_DIARIO_SEGURO * qtdDias);
    }

    @Override
    public double getValorTotal() {
        return itemDecorado.getValorTotal() + (VALOR_DIARIO_SEGURO * qtdDias);
    }
}
