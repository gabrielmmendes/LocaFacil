package br.com.locafacil.model;

import java.math.BigDecimal;

public class CategoriaVeiculo {
    private Long idCategoria;
    private String nomeCategoria;
    private BigDecimal valorDiariaPadrao;
    private String descricao;

    public CategoriaVeiculo(Long id, String nome, BigDecimal valorPadrao, String descricao) {
        this.idCategoria = id;
        this.nomeCategoria = nome;
        this.valorDiariaPadrao = valorPadrao;
        this.descricao = descricao;
    }

    public Long getId() { return idCategoria; }
    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String n) { nomeCategoria = n; }
    public BigDecimal getValorDiariaPadrao() { return valorDiariaPadrao; }
    public void setValorDiariaPadrao(BigDecimal v) { valorDiariaPadrao = v; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String d) { descricao = d; }
    public String toString() { return nomeCategoria + " - R$ " + valorDiariaPadrao; }
}
