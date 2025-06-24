package br.com.locafacil.model;

import java.math.BigDecimal;

/**
 * Classe que representa uma multa ou taxa extra aplicada a um contrato de locação.
 * Pode ser utilizada para registrar multas por atraso, danos ao veículo ou outros serviços extras.
 */
public class MultaExtra {
    private Long idMulta;
    private TipoMulta tipo;
    private String descricao;
    private BigDecimal valor;

    /**
     * Construtor para criar uma nova multa ou taxa extra.
     * 
     * @param id Identificador único da multa
     * @param tipo Tipo da multa (ATRASO, DANO, SERVICO)
     * @param descricao Descrição detalhada da multa ou serviço
     * @param valor Valor monetário da multa ou serviço
     */
    public MultaExtra(Long id, TipoMulta tipo, String descricao, BigDecimal valor) {
        this.idMulta = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
    }

    /**
     * Retorna o ID da multa.
     * @return Identificador único da multa
     */
    public Long getId() { return idMulta; }

    /**
     * Retorna o tipo da multa.
     * @return Tipo da multa (ATRASO, DANO, SERVICO)
     */
    public TipoMulta getTipo() { return tipo; }

    /**
     * Retorna a descrição da multa ou serviço.
     * @return Descrição detalhada
     */
    public String getDescricao() { return descricao; }

    /**
     * Retorna o valor monetário da multa ou serviço.
     * @return Valor da multa
     */
    public BigDecimal getValor() { return valor; }

    /**
     * Retorna uma representação em string da multa.
     * @return String no formato "tipo: descrição - R$ valor"
     */
    public String toString() { return tipo + ": " + descricao + " - R$ " + valor; }
}
