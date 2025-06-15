package br.com.locafacil.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContratoLocacao {
    private Long idContrato;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicioPrevista;
    private LocalDate dataFimPrevista;
    private LocalDateTime dataRetirada;
    private LocalDateTime dataDevolucao;
    private int quilometragemInicial;
    private Integer quilometragemFinal;
    private BigDecimal valorDiarioContratado;
    private BigDecimal valorParcial;
    private BigDecimal valorMultas = BigDecimal.ZERO;
    private BigDecimal valorExtras = BigDecimal.ZERO;
    private BigDecimal valorTotal;
    private StatusContrato statusContrato;
    private List<MultaExtra> multasExtras = new ArrayList<>();
    private String observacoesDanos;

    public ContratoLocacao(Long id, Cliente cliente, Veiculo veiculo, LocalDate dataInicioPrev, LocalDate dataFimPrev, int quilometragemInicial, BigDecimal valorDiario) {
        this.idContrato = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicioPrevista = dataInicioPrev;
        this.dataFimPrevista = dataFimPrev;
        this.quilometragemInicial = quilometragemInicial;
        this.valorDiarioContratado = valorDiario;
        this.statusContrato = StatusContrato.RESERVA;
        calcularValorParcial();
    }

    public Long getId() { return idContrato; }
    public Cliente getCliente() { return cliente; }
    public Veiculo getVeiculo() { return veiculo; }
    public LocalDate getDataInicioPrevista() { return dataInicioPrevista; }
    public void setDataInicioPrevista(LocalDate d) { dataInicioPrevista = d; }
    public LocalDate getDataFimPrevista() { return dataFimPrevista; }
    public void setDataFimPrevista(LocalDate d) { dataFimPrevista = d; }
    public LocalDateTime getDataRetirada() { return dataRetirada; }
    public void setDataRetirada(LocalDateTime d) { dataRetirada = d; }
    public LocalDateTime getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDateTime d) { dataDevolucao = d; }
    public int getQuilometragemInicial() { return quilometragemInicial; }
    public void setQuilometragemInicial(int q) { quilometragemInicial = q; }
    public Integer getQuilometragemFinal() { return quilometragemFinal; }
    public void setQuilometragemFinal(int q) { quilometragemFinal = q; }
    public BigDecimal getValorDiarioContratado() { return valorDiarioContratado; }
    public BigDecimal getValorParcial() { return valorParcial; }

    public BigDecimal calcularValorParcial() {
        long dias = java.time.temporal.ChronoUnit.DAYS.between(dataInicioPrevista, dataFimPrevista);
        if (dias == 0) dias = 1; // Mínimo de 1 dia
        valorParcial = valorDiarioContratado.multiply(BigDecimal.valueOf(dias));
        return valorParcial;
    }

    public BigDecimal getValorMultas() { return valorMultas; }
    public BigDecimal getValorExtras() { return valorExtras; }

    public void calcularMultas() {
        valorMultas = multasExtras.stream()
                .map(MultaExtra::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calcularExtras() {
        // Simplesmente recalcula como se fossem todas as multas de tipo != multa.
        calcularMultas(); // ou fazer algo separado se quiser separar Multas e Extras
    }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void calcularValorTotal() {
        valorTotal = valorParcial.add(valorMultas).add(valorExtras);
    }

    public StatusContrato getStatusContrato() { return statusContrato; }
    public void setStatusContrato(StatusContrato s) { statusContrato = s; }
    public void adicionarMultaExtra(MultaExtra m) { multasExtras.add(m); }
    public List<MultaExtra> getMultasExtras() { return multasExtras; }
    public String getObservacoesDanos() { return observacoesDanos; }
    public void setObservacoesDanos(String obs) { observacoesDanos = obs; }
    public String toString() { return "Contrato " + idContrato + " - Cliente: " + cliente.getNome(); }
}
