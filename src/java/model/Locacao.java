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
    
    // Getters e Setters

    public int getIdLocacao() {
        return idLocacao;
    }

    public void setIdLocacao(int idLocacao) {
        this.idLocacao = idLocacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public int getQtdDias() {
        return qtdDias;
    }

    public void setQtdDias(int qtdDias) {
        this.qtdDias = qtdDias;
    }

    public double getSeguroLocacao() {
        return seguroLocacao;
    }

    public void setSeguroLocacao(double seguroLocacao) {
        this.seguroLocacao = seguroLocacao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    
}