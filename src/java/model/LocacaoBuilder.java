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
    
    // Construtor Vazio
    public LocacaoBuilder(){
    }
    
    // Métodos "set" que retornam o próprio Builder
    public LocacaoBuilder comId(int idLocacao) {
        this.idLocacao = idLocacao;
        return this;
    }

    public LocacaoBuilder paraUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public LocacaoBuilder comVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        return this;
    }

    public LocacaoBuilder comQtdDias(int qtdDias) {
        this.qtdDias = qtdDias;
        return this;
    }

    public LocacaoBuilder comSeguro(double seguroLocacao) {
        this.seguroLocacao = seguroLocacao;
        return this;
    }

    public LocacaoBuilder comDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
        return this;
    }

    public LocacaoBuilder comDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
        return this;
    }
}
