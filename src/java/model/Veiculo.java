package model;

public class Veiculo {
    
    // Atributos
    private int idVeiculo;
    private Placa placaVeiculo;
    private String modeloVeiculo;
    private String corVeiculo;
    private double valorDiaria;
    private String funcionalidadeVeiculo;
    private boolean disponibilidade;
    private boolean arCondicionadoVeiculo;
    private String tipoCambio;
    
    // Metodos contrutores
    public Veiculo() {
    }

    public Veiculo(int idVeiculo, String placaVeiculo, String modeloVeiculo, String corVeiculo, double valorDiaria, String funcionalidadeVeiculo, boolean disponibilidade, boolean arCondicionadoVeiculo, String tipoCambio) {
        this.idVeiculo = idVeiculo;
        this.placaVeiculo = placaVeiculo != null ? new Placa(placaVeiculo) : null;
        this.modeloVeiculo = modeloVeiculo;
        this.corVeiculo = corVeiculo;
        this.valorDiaria = valorDiaria;
        this.funcionalidadeVeiculo = funcionalidadeVeiculo;
        this.disponibilidade = disponibilidade;
        this.arCondicionadoVeiculo = arCondicionadoVeiculo;
        this.tipoCambio = tipoCambio;
    }

    public Veiculo(int idVeiculo, Placa placaVeiculo, String modeloVeiculo, String corVeiculo, double valorDiaria, String funcionalidadeVeiculo, boolean disponibilidade, boolean arCondicionadoVeiculo, String tipoCambio) {
        this.idVeiculo = idVeiculo;
        this.placaVeiculo = placaVeiculo;
        this.modeloVeiculo = modeloVeiculo;
        this.corVeiculo = corVeiculo;
        this.valorDiaria = valorDiaria;
        this.funcionalidadeVeiculo = funcionalidadeVeiculo;
        this.disponibilidade = disponibilidade;
        this.arCondicionadoVeiculo = arCondicionadoVeiculo;
        this.tipoCambio = tipoCambio;
    }
    
    // Getters e Setters

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public Placa getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(Placa placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo != null ? new Placa(placaVeiculo) : null;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public String getCorVeiculo() {
        return corVeiculo;
    }

    public void setCorVeiculo(String corVeiculo) {
        this.corVeiculo = corVeiculo;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public String getFuncionalidadeVeiculo() {
        return funcionalidadeVeiculo;
    }

    public void setFuncionalidadeVeiculo(String funcionalidadeVeiculo) {
        this.funcionalidadeVeiculo = funcionalidadeVeiculo;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public boolean isArCondicionadoVeiculo() {
        return arCondicionadoVeiculo;
    }

    public void setArCondicionadoVeiculo(boolean arCondicionadoVeiculo) {
        this.arCondicionadoVeiculo = arCondicionadoVeiculo;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    
}