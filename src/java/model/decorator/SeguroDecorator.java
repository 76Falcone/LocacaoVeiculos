package model.decorator;

public abstract class SeguroDecorator implements ItemLocacao {
    protected ItemLocacao itemDecorado;

    public SeguroDecorator(ItemLocacao itemDecorado) {
        this.itemDecorado = itemDecorado;
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao();
    }

    @Override
    public double getValorSeguro() {
        return itemDecorado.getValorSeguro();
    }

    @Override
    public double getValorTotal() {
        return itemDecorado.getValorTotal();
    }
}
