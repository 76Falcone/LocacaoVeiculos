package model;

import java.time.LocalDate;

/**
 *
 * @author 76Falcone
 */
public class LocacaoBuilder {
    
    // Atributos
    private int idLocacao;
    private Usuario usuario;
    private Veiculo veiculo;
    private int qtdDias;
    private double seguroLocacao;
    private double valorTotal;
    private LocalDate dataRetirada;
    private LocalDate dataEntrega;
}
