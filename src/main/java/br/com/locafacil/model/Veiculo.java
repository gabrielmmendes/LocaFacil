package br.com.locafacil.model;

import java.math.BigDecimal;

/**
 * Representa um veículo da frota da locadora.
 */
public class Veiculo {

    /** Identificador único do veículo. */
    private Long idVeiculo;

    /** Placa do veículo. */
    private String placa;

    /** Modelo do veículo (ex: Corolla, Uno, Civic). */
    private String modelo;

    /** Categoria do veículo (ex: SUV, Hatch, Sedan). */
    private String categoria;

    /** Cor do veículo. */
    private String cor;

    /** Ano de fabricação do veículo. */
    private int anoFabricacao;

    /** Valor da diária de locação do veículo. */
    private BigDecimal valorDiaria;

    /** Status atual do veículo. */
    private StatusVeiculo status;

    /**
     * Construtor da classe Veiculo.
     *
     * @param id            Identificador do veículo.
     * @param placa         Placa do veículo.
     * @param modelo        Modelo do veículo.
     * @param categoria     Categoria do veículo.
     * @param cor           Cor do veículo.
     * @param ano           Ano de fabricação.
     * @param valorDiaria   Valor da diária de locação.
     */
    public Veiculo(Long id, String placa, String modelo, String categoria, String cor, int ano, BigDecimal valorDiaria) {
        this.idVeiculo = id;
        this.placa = placa;
        this.modelo = modelo;
        this.categoria = categoria;
        this.cor = cor;
        this.anoFabricacao = ano;
        this.valorDiaria = valorDiaria;
        this.status = StatusVeiculo.DISPONIVEL;
    }

    /**
     * Obtém o identificador único do veículo.
     * @return o identificador do veículo.
     */
    public Long getId() { return idVeiculo; }

    /**
     * Obtém a placa do veículo.
     * @return a placa do veículo.
     */
    public String getPlaca() { return placa; }

    /**
     * Define a placa do veículo.
     * @param p define a placa do veículo.
     */
    public void setPlaca(String p) { placa = p; }

    /**
     * Obtém o modelo do veículo.
     * @return o modelo do veículo.
     */
    public String getModelo() { return modelo; }

    /**
     * Define o modelo do veículo.
     * @param m define o modelo do veículo.
     */
    public void setModelo(String m) { modelo = m; }

    /**
     * Obtém a categoria do veículo.
     * @return a categoria do veículo.
     */
    public String getCategoria() { return categoria; }

    /**
     * Define a categoria do veículo.
     * @param c define a categoria do veículo.
     */
    public void setCategoria(String c) { categoria = c; }

    /**
     * Obtém a cor do veículo.
     * @return a cor do veículo.
     */
    public String getCor() { return cor; }

    /**
     * Define a cor do veículo.
     * @param c define a cor do veículo.
     */
    public void setCor(String c) { cor = c; }

    /**
     * Obtém o ano de fabricação do veículo.
     * @return o ano de fabricação do veículo.
     */
    public int getAnoFabricacao() { return anoFabricacao; }

    /**
     * Define o ano de fabricação do veículo.
     * @param a define o ano de fabricação do veículo.
     */
    public void setAnoFabricacao(int a) { anoFabricacao = a; }

    /**
     * Obtém o valor da diária de locação do veículo.
     * @return o valor da diária do veículo.
     */
    public BigDecimal getValorDiaria() { return valorDiaria; }

    /**
     * Define o valor da diária de locação do veículo.
     * @param v define o valor da diária do veículo.
     */
    public void setValorDiaria(BigDecimal v) { valorDiaria = v; }

    /**
     * Obtém o status atual do veículo.
     * @return o status atual do veículo.
     */
    public StatusVeiculo getStatus() { return status; }

    /**
     * Define o status atual do veículo.
     * @param s define o status atual do veículo.
     */
    public void setStatus(StatusVeiculo s) { status = s; }

    /**
     * Retorna uma representação simplificada do veículo para exibição.
     *
     * @return uma string no formato "Modelo - Placa".
     */
    public String toString() {
        return modelo + " - " + placa;
    }
}
