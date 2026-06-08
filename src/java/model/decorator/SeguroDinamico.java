package model.decorator;

import model.TipoSeguro;

/**
 * Decorator dinâmico para cobrir qualquer seguro cadastrado pelo administrador
 * no banco de dados, decidindo se é taxa percentual ou valor fixo de forma automática.
 *
 * @author 76Falcone
 */
public class SeguroDinamico extends SeguroDecorator {

    private final int idSeguro;
    private final String descricaoSeguro;
    private final double valor;
    private final boolean percentual;

    public SeguroDinamico(ItemLocacao itemDecorado, TipoSeguro ts) {
        super(itemDecorado);
        this.idSeguro = ts.getId();
        this.descricaoSeguro = ts.getTipo();
        this.valor = ts.getValor();
        this.percentual = ts.isPercentual();
    }

    @Override
    public String getDescricao() {
        if (percentual) {
            int pct = (int) Math.round(valor * 100);
            return itemDecorado.getDescricao() + " + " + descricaoSeguro + " (" + pct + "%)";
        } else {
            return itemDecorado.getDescricao() + " + " + descricaoSeguro;
        }
    }

    @Override
    public double getValorSeguro() {
        if (percentual) {
            double valorBase = itemDecorado.getValorTotal() - itemDecorado.getValorSeguro();
            return itemDecorado.getValorSeguro() + (valorBase * valor);
        } else {
            return itemDecorado.getValorSeguro() + valor;
        }
    }

    @Override
    public double getValorTotal() {
        if (percentual) {
            double valorBase = itemDecorado.getValorTotal() - itemDecorado.getValorSeguro();
            return itemDecorado.getValorTotal() + (valorBase * valor);
        } else {
            return itemDecorado.getValorTotal() + valor;
        }
    }

    public int getIdSeguro() {
        return idSeguro;
    }
}
