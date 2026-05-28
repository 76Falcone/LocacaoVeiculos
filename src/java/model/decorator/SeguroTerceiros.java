package model.decorator;

public class SeguroTerceiros extends SeguroDecorator {

    // Taxa de 10% sobre o valor base da locação (diária × dias)
    private static final double TAXA_SEGURO = 0.10;

    public SeguroTerceiros(ItemLocacao itemDecorado) {
        super(itemDecorado);
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Seguro de Terceiros (10%)";
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
