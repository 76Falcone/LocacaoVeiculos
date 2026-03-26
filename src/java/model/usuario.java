package model;

public class usuario {
    
    // Atributos
    private int idUsuario;
    private String nomeUsuario;
    private String cpfUsuario;
    private String cnhUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private String celularUsuario;
    
    // Metodos contrutores
    public usuario() {
    }

    public usuario(int idUsuario, String nomeUsuario, String cpfUsuario, String cnhUsuario, String emailUsuario, String senhaUsuario, String celularUsuario) {
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.cpfUsuario = cpfUsuario;
        this.cnhUsuario = cnhUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.celularUsuario = celularUsuario;
    }
    
}