package br.com.locafacil.model;

/**
 * Classe que representa um cliente no sistema de locação de veículos.
 * Armazena informações pessoais e de contato do cliente.
 */
public class Cliente {
    private Long idCliente;
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String email;
    private String numeroCNH;

    /**
     * Construtor para criar um novo cliente.
     * 
     * @param id Identificador único do cliente
     * @param nome Nome completo do cliente
     * @param cpfCnpj CPF ou CNPJ do cliente
     * @param telefone Número de telefone para contato
     * @param email Endereço de e-mail do cliente
     * @param numeroCNH Número da Carteira Nacional de Habilitação
     */
    public Cliente(Long id, String nome, String cpfCnpj, String telefone, String email, String numeroCNH) {
        this.idCliente = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.email = email;
        this.numeroCNH = numeroCNH;
    }

    /**
     * Retorna o ID do cliente.
     * @return Identificador único do cliente
     */
    public Long getId() { return idCliente; }

    /**
     * Retorna o nome do cliente.
     * @return Nome completo do cliente
     */
    public String getNome() { return nome; }

    /**
     * Define o nome do cliente.
     * @param n Novo nome do cliente
     */
    public void setNome(String n) { nome = n; }

    /**
     * Retorna o CPF ou CNPJ do cliente.
     * @return CPF ou CNPJ do cliente
     */
    public String getCpfCnpj() { return cpfCnpj; }

    /**
     * Define o CPF ou CNPJ do cliente.
     * @param c Novo CPF ou CNPJ
     */
    public void setCpfCnpj(String c) { cpfCnpj = c; }

    /**
     * Retorna o telefone do cliente.
     * @return Número de telefone do cliente
     */
    public String getTelefone() { return telefone; }

    /**
     * Define o telefone do cliente.
     * @param t Novo número de telefone
     */
    public void setTelefone(String t) { telefone = t; }

    /**
     * Retorna o email do cliente.
     * @return Endereço de email do cliente
     */
    public String getEmail() { return email; }

    /**
     * Define o email do cliente.
     * @param e Novo endereço de email
     */
    public void setEmail(String e) { email = e; }

    /**
     * Retorna o número da CNH do cliente.
     * @return Número da Carteira Nacional de Habilitação
     */
    public String getNumeroCNH() { return numeroCNH; }

    /**
     * Define o número da CNH do cliente.
     * @param n Novo número de CNH
     */
    public void setNumeroCNH(String n) { numeroCNH = n; }

    /**
     * Retorna uma representação em string do cliente.
     * @return String no formato "nome (cpfCnpj)"
     */
    public String toString() { return nome + " (" + cpfCnpj + ")"; }
}
