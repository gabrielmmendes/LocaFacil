package br.com.locafacil.model;

import java.math.BigDecimal;

public class Veiculo {
    private Long idVeiculo;
    private String placa;
    private String modelo;
    private String categoria;
    private String cor;
    private int anoFabricacao;
    private BigDecimal valorDiaria;
    private StatusVeiculo status;

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

    public Long getId() { return idVeiculo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String p) { placa = p; }
    public String getModelo() { return modelo; }
    public void setModelo(String m) { modelo = m; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { categoria = c; }
    public String getCor() { return cor; }
    public void setCor(String c) { cor = c; }
    public int getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(int a) { anoFabricacao = a; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal v) { valorDiaria = v; }
    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo s) { status = s; }
    public String toString() { return modelo + " - " + placa; }
}