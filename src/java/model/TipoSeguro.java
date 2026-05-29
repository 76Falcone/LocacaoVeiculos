package model;

/**
 * Representa um registro da tabela tipo_seguro.
 * Cada seguro pode ser percentual (valor < 1, ex: 0.10 = 10%)
 * ou valor fixo (valor >= 1, ex: 100.00 = R$ 100,00).
 *
 * @author 76Falcone
 */
public class TipoSeguro {

    private int id;
    private String tipo;
    private double valor;

    public TipoSeguro() {
    }

    public TipoSeguro(int id, String tipo, double valor) {
        this.id    = id;
        this.tipo  = tipo;
        this.valor = valor;
    }

    // ── Getters e Setters ──────────────────────────────────────

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * Retorna true se o seguro for percentual (valor < 1).
     * Ex: 0.10 = 10% sobre o valor base das diárias.
     */
    public boolean isPercentual() {
        return valor < 1.0;
    }
}
