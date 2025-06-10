package br.com.locafacil.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {
    private Long idPagamento;
    private ContratoLocacao contrato;
    private LocalDateTime dataPagamento;
    private MetodoPagamento metodoPagamento;
    private BigDecimal valorPago;
    private StatusPagamento statusPagamento;

    public Pagamento(Long id, ContratoLocacao contrato, LocalDateTime dataPag, MetodoPagamento metodo, BigDecimal valorPago) {
        this.idPagamento = id;
        this.contrato = contrato;
        this.dataPagamento = dataPag;
        this.metodoPagamento = metodo;
        this.valorPago = valorPago;
        this.statusPagamento = StatusPagamento.PENDENTE;
    }

    public Long getId() { return idPagamento; }
    public ContratoLocacao getContrato() { return contrato; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public BigDecimal getValorPago() { return valorPago; }
    public StatusPagamento getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(StatusPagamento s) { statusPagamento = s; }
    public String toString() { return "Pagamento " + idPagamento + ", valor: R$ " + valorPago; }
}
