package model;

public class Usuario {
    
    // Atributos
    private int idUsuario;
    private String nomeUsuario;
    private String cpfUsuario;
    private String cnhUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private String celularUsuario;
    
    // Metodos contrutores
    public Usuario() {
    }

    public Usuario(int idUsuario, String nomeUsuario, String cpfUsuario, String cnhUsuario, String emailUsuario, String senhaUsuario, String celularUsuario) {
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.cpfUsuario = cpfUsuario;
        this.cnhUsuario = cnhUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.celularUsuario = celularUsuario;
    }
    
    // Getters e Setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getCnhUsuario() {
        return cnhUsuario;
    }

    public void setCnhUsuario(String cnhUsuario) {
        this.cnhUsuario = cnhUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getCelularUsuario() {
        return celularUsuario;
    }

    public void setCelularUsuario(String celularUsuario) {
        this.celularUsuario = celularUsuario;
    }
    
}