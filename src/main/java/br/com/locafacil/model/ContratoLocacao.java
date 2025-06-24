package br.com.locafacil.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um contrato de locação de veículo.
 * Contém informações sobre o cliente, veículo, datas, valores e status do contrato.
 * Gerencia o ciclo de vida completo de uma locação, desde a reserva até a devolução.
 */
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

    /**
     * Construtor para criar um novo contrato de locação.
     * 
     * @param id Identificador único do contrato
     * @param cliente Cliente que está realizando a locação
     * @param veiculo Veículo que será locado
     * @param dataInicioPrev Data prevista para início da locação
     * @param dataFimPrev Data prevista para término da locação
     * @param quilometragemInicial Quilometragem do veículo no momento da locação
     * @param valorDiario Valor diário contratado para a locação
     */
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

    /**
     * Retorna o ID do contrato.
     * @return Identificador único do contrato
     */
    public Long getId() { return idContrato; }

    /**
     * Retorna o cliente associado ao contrato.
     * @return Objeto Cliente
     */
    public Cliente getCliente() { return cliente; }

    /**
     * Retorna o veículo associado ao contrato.
     * @return Objeto Veiculo
     */
    public Veiculo getVeiculo() { return veiculo; }

    /**
     * Retorna a data prevista para início da locação.
     * @return Data de início prevista
     */
    public LocalDate getDataInicioPrevista() { return dataInicioPrevista; }

    /**
     * Define a data prevista para início da locação.
     * @param d Nova data de início
     */
    public void setDataInicioPrevista(LocalDate d) { dataInicioPrevista = d; }

    /**
     * Retorna a data prevista para término da locação.
     * @return Data de término prevista
     */
    public LocalDate getDataFimPrevista() { return dataFimPrevista; }

    /**
     * Define a data prevista para término da locação.
     * @param d Nova data de término
     */
    public void setDataFimPrevista(LocalDate d) { dataFimPrevista = d; }

    /**
     * Retorna a data e hora efetiva da retirada do veículo.
     * @return Data e hora da retirada
     */
    public LocalDateTime getDataRetirada() { return dataRetirada; }

    /**
     * Define a data e hora efetiva da retirada do veículo.
     * @param d Data e hora da retirada
     */
    public void setDataRetirada(LocalDateTime d) { dataRetirada = d; }

    /**
     * Retorna a data e hora efetiva da devolução do veículo.
     * @return Data e hora da devolução
     */
    public LocalDateTime getDataDevolucao() { return dataDevolucao; }

    /**
     * Define a data e hora efetiva da devolução do veículo.
     * @param d Data e hora da devolução
     */
    public void setDataDevolucao(LocalDateTime d) { dataDevolucao = d; }

    /**
     * Retorna a quilometragem inicial do veículo no momento da locação.
     * @return Quilometragem inicial
     */
    public int getQuilometragemInicial() { return quilometragemInicial; }

    /**
     * Define a quilometragem inicial do veículo.
     * @param q Nova quilometragem inicial
     */
    public void setQuilometragemInicial(int q) { quilometragemInicial = q; }

    /**
     * Retorna a quilometragem final do veículo no momento da devolução.
     * @return Quilometragem final ou null se o veículo ainda não foi devolvido
     */
    public Integer getQuilometragemFinal() { return quilometragemFinal; }

    /**
     * Define a quilometragem final do veículo no momento da devolução.
     * @param q Quilometragem final
     */
    public void setQuilometragemFinal(int q) { quilometragemFinal = q; }

    /**
     * Retorna o valor diário contratado para a locação.
     * @return Valor diário contratado
     */
    public BigDecimal getValorDiarioContratado() { return valorDiarioContratado; }

    /**
     * Retorna o valor parcial do contrato (sem multas e extras).
     * @return Valor parcial do contrato
     */
    public BigDecimal getValorParcial() { return valorParcial; }

    /**
     * Calcula o valor parcial do contrato baseado no período de locação e valor diário.
     * Se o período for menor que 1 dia, considera como 1 dia para cálculo.
     * 
     * @return Valor parcial calculado do contrato
     */
    public BigDecimal calcularValorParcial() {
        long dias = java.time.temporal.ChronoUnit.DAYS.between(dataInicioPrevista, dataFimPrevista);
        if (dias == 0) dias = 1; // Mínimo de 1 dia
        valorParcial = valorDiarioContratado.multiply(BigDecimal.valueOf(dias));
        return valorParcial;
    }

    /**
     * Retorna o valor total das multas aplicadas ao contrato.
     * @return Valor total das multas
     */
    public BigDecimal getValorMultas() { return valorMultas; }

    /**
     * Retorna o valor total dos extras aplicados ao contrato.
     * @return Valor total dos extras
     */
    public BigDecimal getValorExtras() { return valorExtras; }

    /**
     * Calcula o valor total das multas aplicadas ao contrato.
     * Soma todos os valores das multas extras registradas.
     */
    public void calcularMultas() {
        valorMultas = multasExtras.stream()
                .map(MultaExtra::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o valor total dos extras aplicados ao contrato.
     * Atualmente utiliza o mesmo cálculo das multas, mas pode ser estendido
     * para calcular extras de forma separada.
     */
    public void calcularExtras() {
        // Simplesmente recalcula como se fossem todas as multas de tipo != multa.
        calcularMultas(); // ou fazer algo separado se quiser separar Multas e Extras
    }

    /**
     * Retorna o valor total do contrato.
     * @return Valor total do contrato incluindo valor parcial, multas e extras
     */
    public BigDecimal getValorTotal() { return valorTotal; }

    /**
     * Calcula o valor total do contrato somando o valor parcial, multas e extras.
     * Deve ser chamado após calcular os valores parciais, multas e extras.
     */
    public void calcularValorTotal() {
        valorTotal = valorParcial.add(valorMultas).add(valorExtras);
    }

    /**
     * Retorna o status atual do contrato.
     * @return Status atual do contrato (RESERVA, ATIVO, ENCERRADO, CANCELADO)
     */
    public StatusContrato getStatusContrato() { return statusContrato; }

    /**
     * Define o status do contrato.
     * @param s Novo status do contrato
     */
    public void setStatusContrato(StatusContrato s) { statusContrato = s; }

    /**
     * Adiciona uma multa ou taxa extra ao contrato.
     * @param m Objeto MultaExtra a ser adicionado
     */
    public void adicionarMultaExtra(MultaExtra m) { multasExtras.add(m); }

    /**
     * Retorna a lista de multas e taxas extras do contrato.
     * @return Lista de objetos MultaExtra
     */
    public List<MultaExtra> getMultasExtras() { return multasExtras; }

    /**
     * Retorna as observações sobre danos no veículo.
     * @return String com observações sobre danos
     */
    public String getObservacoesDanos() { return observacoesDanos; }

    /**
     * Define as observações sobre danos no veículo.
     * @param obs String com observações sobre danos
     */
    public void setObservacoesDanos(String obs) { observacoesDanos = obs; }

    /**
     * Retorna uma representação em string do contrato.
     * @return String no formato "Contrato [id] - Cliente: [nome]"
     */
    public String toString() { return "Contrato " + idContrato + " - Cliente: " + cliente.getNome(); }
}
