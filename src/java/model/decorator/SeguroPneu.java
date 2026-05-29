package model.decorator;

/**
 * Decorator: Seguro Pneu.
 * Adiciona um valor fixo ao total da locação.
 * O valor é lido do banco de dados (tabela tipo_seguro) e passado via construtor.
 *
 * @author 76Falcone
 */
public class SeguroPneu extends SeguroDecorator {

    private final int    idSeguro;
    private final double valorFixo;

    public SeguroPneu(ItemLocacao itemDecorado, int idSeguro, double valorFixo) {
        super(itemDecorado);
        this.idSeguro  = idSeguro;
        this.valorFixo = valorFixo;
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Pneu";
    }

    @Override
    public double getValorSeguro() {
        return valorFixo;
    }

    @Override
    public double getValorTotal() {
        return itemDecorado.getValorTotal() + getValorSeguro();
    }

    public int getIdSeguro() {
        return idSeguro;
    }
}
