package model;

import java.time.LocalDate;

/**
 *
 * @author 76Falcone
 */
public class Locacao {
    
    // Atributos
    private int idLocacao;
    private Usuario usuario;       
    private Veiculo veiculo;     
    private int qtdDias;
    private double seguroLocacao;
    private double valorTotal;
    private LocalDate dataRetirada; 
    private LocalDate dataEntrega;

    // Construtores
    public Locacao() {
    }

    public Locacao(int idLocacao, Usuario usuario, Veiculo veiculo, int qtdDias, double seguroLocacao, double valorTotal, LocalDate dataRetirada, LocalDate dataEntrega) {
        this.idLocacao = idLocacao;
        this.usuario = usuario;
        this.veiculo = veiculo;
        this.qtdDias = qtdDias;
        this.seguroLocacao = seguroLocacao;
        this.valorTotal = valorTotal;
        this.dataRetirada = dataRetirada;
        this.dataEntrega = dataEntrega;
    }
    
    
}
