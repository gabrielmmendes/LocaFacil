package br.com.locafacil.model;

import java.math.BigDecimal;

/**
 * Classe que representa uma categoria de veículo no sistema de locação.
 * Cada categoria possui um valor padrão de diária e uma descrição.
 */
public class CategoriaVeiculo {
    private Long idCategoria;
    private String nomeCategoria;
    private BigDecimal valorDiariaPadrao;
    private String descricao;

    /**
     * Construtor para criar uma nova categoria de veículo.
     * 
     * @param id Identificador único da categoria
     * @param nome Nome da categoria
     * @param valorPadrao Valor padrão da diária para esta categoria
     * @param descricao Descrição detalhada da categoria
     */
    public CategoriaVeiculo(Long id, String nome, BigDecimal valorPadrao, String descricao) {
        this.idCategoria = id;
        this.nomeCategoria = nome;
        this.valorDiariaPadrao = valorPadrao;
        this.descricao = descricao;
    }

    /**
     * Retorna o ID da categoria.
     * @return Identificador único da categoria
     */
    public Long getId() { return idCategoria; }

    /**
     * Retorna o nome da categoria.
     * @return Nome da categoria
     */
    public String getNomeCategoria() { return nomeCategoria; }

    /**
     * Define o nome da categoria.
     * @param n Novo nome da categoria
     */
    public void setNomeCategoria(String n) { nomeCategoria = n; }

    /**
     * Retorna o valor padrão da diária para esta categoria.
     * @return Valor padrão da diária
     */
    public BigDecimal getValorDiariaPadrao() { return valorDiariaPadrao; }

    /**
     * Define o valor padrão da diária para esta categoria.
     * @param v Novo valor padrão da diária
     */
    public void setValorDiariaPadrao(BigDecimal v) { valorDiariaPadrao = v; }

    /**
     * Retorna a descrição da categoria.
     * @return Descrição detalhada da categoria
     */
    public String getDescricao() { return descricao; }

    /**
     * Define a descrição da categoria.
     * @param d Nova descrição da categoria
     */
    public void setDescricao(String d) { descricao = d; }

    /**
     * Retorna uma representação em string da categoria.
     * @return String no formato "nomeCategoria - R$ valorDiariaPadrao"
     */
    public String toString() { return nomeCategoria + " - R$ " + valorDiariaPadrao; }
}
