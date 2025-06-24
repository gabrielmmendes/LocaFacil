package br.com.locafacil.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe que representa um pagamento no sistema de locação.
 * Armazena informações sobre pagamentos realizados para contratos de locação,
 * incluindo valor, data, método e status do pagamento.
 */
public class Pagamento {
    private Long idPagamento;
    private ContratoLocacao contrato;
    private LocalDateTime dataPagamento;
    private MetodoPagamento metodoPagamento;
    private BigDecimal valorPago;
    private StatusPagamento statusPagamento;

    /**
     * Construtor para criar um novo pagamento.
     * 
     * @param id Identificador único do pagamento
     * @param contrato Contrato de locação associado ao pagamento
     * @param dataPag Data e hora em que o pagamento foi realizado
     * @param metodo Método utilizado para o pagamento
     * @param valorPago Valor monetário do pagamento
     */
    public Pagamento(Long id, ContratoLocacao contrato, LocalDateTime dataPag, MetodoPagamento metodo, BigDecimal valorPago) {
        this.idPagamento = id;
        this.contrato = contrato;
        this.dataPagamento = dataPag;
        this.metodoPagamento = metodo;
        this.valorPago = valorPago;
        this.statusPagamento = StatusPagamento.PENDENTE;
    }

    /**
     * Retorna o ID do pagamento.
     * @return Identificador único do pagamento
     */
    public Long getId() { return idPagamento; }

    /**
     * Retorna o contrato associado ao pagamento.
     * @return Contrato de locação
     */
    public ContratoLocacao getContrato() { return contrato; }

    /**
     * Retorna a data e hora do pagamento.
     * @return Data e hora do pagamento
     */
    public LocalDateTime getDataPagamento() { return dataPagamento; }

    /**
     * Retorna o método utilizado para o pagamento.
     * @return Método de pagamento
     */
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }

    /**
     * Retorna o valor do pagamento.
     * @return Valor monetário pago
     */
    public BigDecimal getValorPago() { return valorPago; }

    /**
     * Retorna o status atual do pagamento.
     * @return Status do pagamento (PENDENTE, CONFIRMADO, CANCELADO)
     */
    public StatusPagamento getStatusPagamento() { return statusPagamento; }

    /**
     * Define o status do pagamento.
     * @param s Novo status do pagamento
     */
    public void setStatusPagamento(StatusPagamento s) { statusPagamento = s; }

    /**
     * Retorna uma representação em string do pagamento.
     * @return String no formato "Pagamento [id], valor: R$ [valor]"
     */
    public String toString() { return "Pagamento " + idPagamento + ", valor: R$ " + valorPago; }
}
