package br.com.locafacil.model;

public class Cliente {
    private Long idCliente;
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String email;
    private String numeroCNH;

    public Cliente(Long id, String nome, String cpfCnpj, String telefone, String email, String numeroCNH) {
        this.idCliente = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.email = email;
        this.numeroCNH = numeroCNH;
    }

    public Long getId() { return idCliente; }
    public String getNome() { return nome; }
    public void setNome(String n) { nome = n; }
    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String c) { cpfCnpj = c; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String t) { telefone = t; }
    public String getEmail() { return email; }
    public void setEmail(String e) { email = e; }
    public String getNumeroCNH() { return numeroCNH; }
    public void setNumeroCNH(String n) { numeroCNH = n; }
    public String toString() { return nome + " (" + cpfCnpj + ")"; }
}
