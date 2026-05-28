package model.decorator;

public class SeguroCoberturaTotal extends SeguroDecorator {

    // Taxa de 15% sobre o valor base da locação (diária × dias)
    private static final double TAXA_SEGURO = 0.15;

    public SeguroCoberturaTotal(ItemLocacao itemDecorado) {
        super(itemDecorado);
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Seguro Cobertura Total (15%)";
    }

    @Override
    public double getValorSeguro() {
        return itemDecorado.getValorTotal() * TAXA_SEGURO;
    }

    @Override
    public double getValorTotal() {
        return itemDecorado.getValorTotal() + getValorSeguro();
    }
}
