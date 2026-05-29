package model.decorator;

/**
 * Decorator: Seguro Vidros e Espelhos.
 * Adiciona um valor fixo ao total da locação.
 * O valor é lido do banco de dados (tabela tipo_seguro) e passado via construtor.
 *
 * @author 76Falcone
 */
public class SeguroVidrosEspelhos extends SeguroDecorator {

    private final int    idSeguro;
    private final double valorFixo;

    public SeguroVidrosEspelhos(ItemLocacao itemDecorado, int idSeguro, double valorFixo) {
        super(itemDecorado);
        this.idSeguro  = idSeguro;
        this.valorFixo = valorFixo;
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Vidros e Espelhos";
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
