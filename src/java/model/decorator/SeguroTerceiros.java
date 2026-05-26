package model.decorator;

public class SeguroTerceiros extends SeguroDecorator {
    // Valor fixo de exemplo por dia, ou total. Aqui vamos considerar um valor diário.
    private static final double VALOR_DIARIO_SEGURO = 30.00;
    private int qtdDias;

    public SeguroTerceiros(ItemLocacao itemDecorado, int qtdDias) {
        super(itemDecorado);
        this.qtdDias = qtdDias;
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Seguro de Terceiros";
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
