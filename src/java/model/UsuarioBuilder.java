package model;

/**
 *
 * @author 76Falcone
 */
public class UsuarioBuilder {
    
    // Atributos
    private int idUsuario;
    private String nomeUsuario;
    private String cpfUsuario;
    private String cnhUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private String celularUsuario;
    
    // Construtor vazio
    public UsuarioBuilder() {
    }
    
    // Métodos "set" que retornam o próprio Builder
    public UsuarioBuilder comId(int idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }
    
    public UsuarioBuilder comNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
        return this;
    }
    
    public UsuarioBuilder comCpfUsuario (String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        return this;
    }
    
    public UsuarioBuilder comCnhUsuario (String cnhUsuario) {
        this.cnhUsuario = cnhUsuario;
        return this;
    }
    
    public UsuarioBuilder comEmailUsuario (String emailUsuario) {
        this.emailUsuario = emailUsuario;
        return this;
    }
    
    public UsuarioBuilder comSenhaUsuario (String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
        return this;
    }
            
    public UsuarioBuilder comCelularUsuario (String celularUsuario) {
        this.celularUsuario = celularUsuario;
        return this;
    }
    
    // Metodo builder
    public Usuario build() {
        return new Usuario (idUsuario, nomeUsuario, cpfUsuario, 
                cnhUsuario, emailUsuario, senhaUsuario, celularUsuario);    
    }
}