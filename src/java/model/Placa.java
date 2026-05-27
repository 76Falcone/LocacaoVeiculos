package model;

public class Placa {
    private final String valor;

    public Placa(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Placa não pode ser nula!");
        }
        String valorNormalizado = valor.toUpperCase().trim();
        if (!valorNormalizado.matches("[A-Z]{3}\\d[A-Z]\\d{2}|[A-Z]{3}\\d{4}")) {
            throw new IllegalArgumentException("Formato de placa inválido!");
        }
        this.valor = valorNormalizado;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placa placa = (Placa) o;
        return valor.equals(placa.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }
}
