package br.com.locafacil.model;

import java.math.BigDecimal;

public class MultaExtra {
    private Long idMulta;
    private TipoMulta tipo;
    private String descricao;
    private BigDecimal valor;

    public MultaExtra(Long id, TipoMulta tipo, String descricao, BigDecimal valor) {
        this.idMulta = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Long getId() { return idMulta; }
    public TipoMulta getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public String toString() { return tipo + ": " + descricao + " - R$ " + valor; }
}
