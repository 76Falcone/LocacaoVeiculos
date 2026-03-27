package model;

public class veiculo {
    
    // Atributos
    private int idVeiculo;
    private String placaVeiculo;
    private String modeloVeiculo;
    private String corVeiculo;
    private double valorDiaria;
    private String funcionalidadeVeiculo;
    private boolean disponibilidade;
    private boolean arCondicionadoVeiculo;
    private String tipoCambio;
    
    // Metodos contrutores
    public veiculo() {
    }

    public veiculo(int idVeiculo, String placaVeiculo, String modeloVeiculo, String corVeiculo, double valorDiaria, String funcionalidadeVeiculo, boolean disponibilidade, boolean arCondicionadoVeiculo, String tipoCambio) {
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
    
}
