package model.decorator;

/**
 * Decorator: Seguro de valor fixo em R$.
 * Utilizado para seguros como Pane Elétrica, Vidros e Espelhos, Pneu,
 * onde o custo adicional é um valor absoluto (ex: R$ 100,00).
 *
 * O valor é lido do banco de dados (tabela tipo_seguro) e passado via construtor.
 *
 * @author 76Falcone
 */
public class SeguroFixo extends SeguroDecorator {

    private final int    idSeguro;
    private final String descricaoSeguro;
    private final double valorFixo; // ex: 100.00 = R$ 100,00

    public SeguroFixo(ItemLocacao itemDecorado, int idSeguro, String descricaoSeguro, double valorFixo) {
        super(itemDecorado);
        this.idSeguro        = idSeguro;
        this.descricaoSeguro = descricaoSeguro;
        this.valorFixo       = valorFixo;
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + " + descricaoSeguro;
    }

    @Override
    public double getValorSeguro() {
        // Valor fixo: não depende do valor das diárias
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
