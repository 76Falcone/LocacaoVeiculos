package model;

/**
 *
 * @author 76Falcone
 */
public class VeiculoBuilder {
        
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
    
    // Construtor vazio
    public VeiculoBuilder() {
    }
    
    // Métodos "set" que retornam o próprio Builder
    public VeiculoBuilder comIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
        return this;
    }
    
    public VeiculoBuilder comPlacaVeiculo (String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
        return this;
    }
    
    public VeiculoBuilder comModeloVeiculo (String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
        return this;
    }
    
    public VeiculoBuilder comCorVeiculo (String corVeiculo) {
        this.corVeiculo = corVeiculo;
        return this;
    }
    
    public VeiculoBuilder comValorDiaria (double valorDiaria) {
        this.valorDiaria = valorDiaria;
        return this;
    }
    
    public VeiculoBuilder comFuncionalidadeVeiculo(String funcionalidadeVeiculo) {
        this.funcionalidadeVeiculo = funcionalidadeVeiculo;
        return this;
    }
    
    public VeiculoBuilder comDisponibilidade (boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
        return this;
    }
    
    public VeiculoBuilder comArCondicionadoVeiculo (boolean arCondicionadoVeiculo) {
        this.arCondicionadoVeiculo = arCondicionadoVeiculo;
        return this;
    }
    
    public VeiculoBuilder comTipoCambio (String tipoCambio) {
        this.tipoCambio = tipoCambio;
        return this;
    }
    
    // Metodo builder
    public Veiculo build() {
    return new Veiculo(idVeiculo, placaVeiculo, modeloVeiculo, corVeiculo, 
                       valorDiaria, funcionalidadeVeiculo, disponibilidade, 
                       arCondicionadoVeiculo, tipoCambio);
    }
}