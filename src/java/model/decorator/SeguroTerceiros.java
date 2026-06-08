package model.decorator;

/**
 * Decorator: Seguro de Terceiros.
 * Aplica um percentual sobre o valor total das diárias.
 * A taxa é lida do banco de dados (tabela tipo_seguro) e passada via construtor.
 *
 * Ex: valor = 0.10 → adiciona 10% sobre o valorBase (diária × dias).
 *
 * @author 76Falcone
 */
public class SeguroTerceiros extends SeguroDecorator {

    private final int    idSeguro;
    private final String descricaoSeguro;
    private final double taxa; // ex: 0.10 = 10%

    public SeguroTerceiros(ItemLocacao itemDecorado, int idSeguro, String descricaoSeguro, double taxa) {
        super(itemDecorado);
        this.idSeguro        = idSeguro;
        this.descricaoSeguro = descricaoSeguro;
        this.taxa            = taxa;
    }

    @Override
    public String getDescricao() {
        int pct = (int) Math.round(taxa * 100);
        return itemDecorado.getDescricao() + " + " + descricaoSeguro + " (" + pct + "%)";
    }

    /**
     * O valor do seguro é a taxa aplicada sobre o valor BASE das diárias.
     * Como o Decorator encadeia corretamente, usamos getValorTotal() do decorado
     * (que já representa o valor base no primeiro nível).
     */
    @Override
    public double getValorSeguro() {
        double valorBase = itemDecorado.getValorTotal() - itemDecorado.getValorSeguro();
        return itemDecorado.getValorSeguro() + (valorBase * taxa);
    }

    @Override
    public double getValorTotal() {
        double valorBase = itemDecorado.getValorTotal() - itemDecorado.getValorSeguro();
        return itemDecorado.getValorTotal() + (valorBase * taxa);
    }

    public int getIdSeguro() {
        return idSeguro;
    }
}
